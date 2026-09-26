package com.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;
    
    private String description;
    private String brand;
    
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private BigDecimal price;
    
    private BigDecimal discountPrice;
    private int discountPercentage;
    
    @NotBlank(message = "Category ID is required")
    private String categoryId;
    
    private List<String> images;
    
    @Min(value = 0, message = "Stock quantity must be positive")
    private int stockQuantity;
    
    private boolean active;
    private boolean featured;
    private List<String> tags;
    
    private String weight;
    private String dimensions;
    private String color;
    private String material;
    private String warranty;
}
