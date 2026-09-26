package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "wishlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wishlist {
    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private List<Product> products = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
            updatedAt = LocalDateTime.now();
        }
    }

    public void removeProduct(Product product) {
        if (products.remove(product)) {
            updatedAt = LocalDateTime.now();
        }
    }

    public boolean containsProduct(Product product) {
        return products.contains(product);
    }

    public void clearProducts() {
        products.clear();
        updatedAt = LocalDateTime.now();
    }
}
