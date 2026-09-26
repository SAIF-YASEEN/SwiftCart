package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    
    private String orderId;
    private String userId;
    
    private BigDecimal amount;
    private String currency;
    
    private PaymentMethod method;
    private PaymentStatus status;
    
    private String transactionId;
    private String gatewayResponse;
    
    private String cardLast4;
    private String cardBrand;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    private LocalDateTime completedAt;
    
    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING, COD, WALLET
    }
    
    public enum PaymentStatus {
        INITIATED, PROCESSING, SUCCESS, FAILED, REFUNDED, CANCELLED
    }
}
