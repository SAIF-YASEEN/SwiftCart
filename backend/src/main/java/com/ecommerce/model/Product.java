package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    
    private String name;
    private String description;
    private String brand;
    
    private BigDecimal price;
    private BigDecimal discountPrice;
    private int discountPercentage;
    
    private String categoryId;
    private String categoryName;
    
    @Builder.Default
    private List<String> images = new ArrayList<>();
    
    private int stockQuantity;
    
    @Builder.Default
    private boolean active = true;
    
    @Builder.Default
    private boolean featured = false;
    
    @Builder.Default
    private double averageRating = 0.0;
    
    @Builder.Default
    private int reviewCount = 0;
    
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    private ProductSpecs specs;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSpecs {
        private String weight;
        private String dimensions;
        private String color;
        private String material;
        private String warranty;
    }
}
