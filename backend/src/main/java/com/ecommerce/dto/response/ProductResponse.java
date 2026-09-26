package com.ecommerce.dto.response;

import com.ecommerce.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private int discountPercentage;
    private String categoryId;
    private String categoryName;
    private List<String> images;
    private int stockQuantity;
    private boolean active;
    private boolean featured;
    private double averageRating;
    private int reviewCount;
    private List<String> tags;
    private ProductSpecsDto specs;
    private LocalDateTime createdAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSpecsDto {
        private String weight;
        private String dimensions;
        private String color;
        private String material;
        private String warranty;
    }
    
    public static ProductResponse fromProduct(Product product) {
        ProductSpecsDto specsDto = null;
        if (product.getSpecs() != null) {
            specsDto = ProductSpecsDto.builder()
                    .weight(product.getSpecs().getWeight())
                    .dimensions(product.getSpecs().getDimensions())
                    .color(product.getSpecs().getColor())
                    .material(product.getSpecs().getMaterial())
                    .warranty(product.getSpecs().getWarranty())
                    .build();
        }
        
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .discountPercentage(product.getDiscountPercentage())
                .categoryId(product.getCategoryId())
                .categoryName(product.getCategoryName())
                .images(product.getImages())
                .stockQuantity(product.getStockQuantity())
                .active(product.isActive())
                .featured(product.isFeatured())
                .averageRating(product.getAverageRating())
                .reviewCount(product.getReviewCount())
                .tags(product.getTags())
                .specs(specsDto)
                .createdAt(product.getCreatedAt())
                .build();
    }
}
