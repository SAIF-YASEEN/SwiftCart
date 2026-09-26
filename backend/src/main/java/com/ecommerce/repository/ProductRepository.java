package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findByActiveTrue(Pageable pageable);
    
    Page<Product> findByCategoryIdAndActiveTrue(String categoryId, Pageable pageable);
    
    Page<Product> findByFeaturedTrueAndActiveTrue(Pageable pageable);
    
    @Query("{'name': {$regex: ?0, $options: 'i'}, 'active': true}")
    Page<Product> searchByName(String name, Pageable pageable);
    
    @Query("{'active': true, 'price': {$gte: ?0, $lte: ?1}}")
    Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    @Query("{'active': true, 'averageRating': {$gte: ?0}}")
    Page<Product> findByMinRating(double minRating, Pageable pageable);
    
    List<Product> findByCategoryId(String categoryId);
    
    @Query("{'active': true, 'tags': {$in: ?0}}")
    Page<Product> findByTagsIn(List<String> tags, Pageable pageable);
    
    long countByActiveTrue();
    
    long countByStockQuantityLessThan(int stockQuantity);
}
