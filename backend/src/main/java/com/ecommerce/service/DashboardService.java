package com.ecommerce.service;

import com.ecommerce.dto.response.DashboardStatsResponse;
import com.ecommerce.model.Order;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    public DashboardStatsResponse getDashboardStats() {
        // Calculate total revenue from completed orders
        List<Order> completedOrders = orderRepository.findByStatus(Order.OrderStatus.DELIVERED);
        BigDecimal totalRevenue = completedOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Also add processing and shipped orders to revenue
        List<Order> processingOrders = orderRepository.findByStatus(Order.OrderStatus.PROCESSING);
        List<Order> shippedOrders = orderRepository.findByStatus(Order.OrderStatus.SHIPPED);
        List<Order> confirmedOrders = orderRepository.findByStatus(Order.OrderStatus.CONFIRMED);
        
        totalRevenue = totalRevenue.add(processingOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRevenue = totalRevenue.add(shippedOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        totalRevenue = totalRevenue.add(confirmedOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        long totalOrders = orderRepository.count();
        long totalProducts = productRepository.count();
        long totalUsers = userRepository.count();
        long pendingOrders = orderRepository.countByStatus(Order.OrderStatus.PENDING);
        long completedOrdersCount = orderRepository.countByStatus(Order.OrderStatus.DELIVERED);
        long activeProducts = productRepository.countByActiveTrue();
        long lowStockProducts = productRepository.countByStockQuantityLessThan(10);
        
        return DashboardStatsResponse.builder()
                .totalRevenue(totalRevenue)
                .totalOrders(totalOrders)
                .totalProducts(totalProducts)
                .totalUsers(totalUsers)
                .pendingOrders(pendingOrders)
                .completedOrders(completedOrdersCount)
                .activeProducts(activeProducts)
                .lowStockProducts(lowStockProducts)
                .build();
    }
}
