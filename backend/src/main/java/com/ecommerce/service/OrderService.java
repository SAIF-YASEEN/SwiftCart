package com.ecommerce.service;

import com.ecommerce.dto.request.OrderRequest;
import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final UserService userService;
    private final PaymentService paymentService;
    
    private static final BigDecimal TAX_RATE = new BigDecimal("0.18"); // 18% tax
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("500");
    private static final BigDecimal SHIPPING_COST = new BigDecimal("50");
    
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = userService.getCurrentUser();
        Cart cart = cartService.getCartEntity();
        
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        
        // Validate stock and create order items
        List<Order.OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItem.getProductId()));
                    
                    if (product.getStockQuantity() < cartItem.getQuantity()) {
                        throw new BadRequestException("Insufficient stock for " + product.getName());
                    }
                    
                    // Reduce stock
                    product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
                    productRepository.save(product);
                    
                    return Order.OrderItem.builder()
                            .productId(cartItem.getProductId())
                            .productName(cartItem.getProductName())
                            .productImage(cartItem.getProductImage())
                            .price(cartItem.getPrice())
                            .quantity(cartItem.getQuantity())
                            .subtotal(cartItem.getSubtotal())
                            .build();
                })
                .collect(Collectors.toList());
        
        BigDecimal subtotal = cart.getTotalPrice();
        BigDecimal shippingCost = subtotal.compareTo(FREE_SHIPPING_THRESHOLD) >= 0 
                ? BigDecimal.ZERO : SHIPPING_COST;
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = subtotal.add(shippingCost).add(tax);
        
        Order.ShippingAddress shippingAddress = Order.ShippingAddress.builder()
                .fullName(request.getShippingAddress().getFullName())
                .phone(request.getShippingAddress().getPhone())
                .street(request.getShippingAddress().getStreet())
                .city(request.getShippingAddress().getCity())
                .state(request.getShippingAddress().getState())
                .zipCode(request.getShippingAddress().getZipCode())
                .country(request.getShippingAddress().getCountry())
                .build();
        
        // Generate order number
        String orderNumber = "ORD" + System.currentTimeMillis();
        String userName = (user.getFirstName() != null ? user.getFirstName() : "") + 
                         (user.getLastName() != null ? " " + user.getLastName() : "");
        
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .userId(user.getId())
                .userName(userName.trim().isEmpty() ? "Customer" : userName.trim())
                .userEmail(user.getEmail())
                .items(orderItems)
                .shippingAddress(shippingAddress)
                .subtotal(subtotal)
                .shippingCost(shippingCost)
                .tax(tax)
                .totalAmount(totalAmount)
                .status(Order.OrderStatus.PENDING)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .notes(request.getNotes())
                .build();
        
        order = orderRepository.save(order);
        
        // Process payment (dummy implementation)
        paymentService.processPayment(order, request.getPaymentMethod());
        
        // Clear cart after successful order
        cartService.clearCart();
        
        return OrderResponse.fromOrder(order);
    }
    
    public Page<OrderResponse> getUserOrders(Pageable pageable) {
        User user = userService.getCurrentUser();
        return orderRepository.findByUserId(user.getId(), pageable)
                .map(OrderResponse::fromOrder);
    }
    
    public OrderResponse getOrderById(String orderId) {
        User user = userService.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        // Verify order belongs to user or user is admin
        if (!order.getUserId().equals(user.getId()) && 
            !user.getRoles().contains(User.Role.ADMIN)) {
            throw new BadRequestException("Access denied");
        }
        
        return OrderResponse.fromOrder(order);
    }
    
    public OrderResponse cancelOrder(String orderId) {
        User user = userService.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        if (!order.getUserId().equals(user.getId())) {
            throw new BadRequestException("Access denied");
        }
        
        if (order.getStatus() != Order.OrderStatus.PENDING && 
            order.getStatus() != Order.OrderStatus.CONFIRMED) {
            throw new BadRequestException("Order cannot be cancelled at this stage");
        }
        
        // Restore stock
        order.getItems().forEach(item -> {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        });
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        return OrderResponse.fromOrder(order);
    }
    
    // Admin methods
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderResponse::fromOrder);
    }
    
    public OrderResponse getOrderByIdAdmin(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        return OrderResponse.fromOrder(order);
    }
    
    public Page<OrderResponse> getOrdersByStatus(Order.OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable).map(OrderResponse::fromOrder);
    }
    
    public OrderResponse updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(newStatus);
        
        if (newStatus == Order.OrderStatus.SHIPPED) {
            order.setShippedAt(LocalDateTime.now());
        } else if (newStatus == Order.OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }
        
        order = orderRepository.save(order);
        return OrderResponse.fromOrder(order);
    }
    
    public OrderResponse updateTrackingNumber(String orderId, String trackingNumber) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        order.setTrackingNumber(trackingNumber);
        order = orderRepository.save(order);
        return OrderResponse.fromOrder(order);
    }
}
