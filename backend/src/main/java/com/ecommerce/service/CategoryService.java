package com.ecommerce.service;

import com.ecommerce.dto.request.CategoryRequest;
import com.ecommerce.dto.response.CategoryResponse;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByActiveTrue().stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }
    
    public List<CategoryResponse> getRootCategories() {
        return categoryRepository.findByParentIdIsNullAndActiveTrue().stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }
    
    public List<CategoryResponse> getSubCategories(String parentId) {
        return categoryRepository.findByParentIdAndActiveTrue(parentId).stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }
    
    public CategoryResponse getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        return CategoryResponse.fromCategory(category);
    }
    
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "slug", slug));
        return CategoryResponse.fromCategory(category);
    }
    
    // Admin methods
    public CategoryResponse createCategory(CategoryRequest request) {
        String slug = generateSlug(request.getName());
        
        if (categoryRepository.existsBySlug(slug)) {
            throw new BadRequestException("Category with this name already exists");
        }
        
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .image(request.getImage())
                .slug(slug)
                .parentId(request.getParentId())
                .displayOrder(request.getDisplayOrder())
                .active(true)
                .build();
        
        category = categoryRepository.save(category);
        return CategoryResponse.fromCategory(category);
    }
    
    public CategoryResponse updateCategory(String categoryId, CategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        if (request.getName() != null && !request.getName().equals(category.getName())) {
            String newSlug = generateSlug(request.getName());
            if (categoryRepository.existsBySlug(newSlug) && !newSlug.equals(category.getSlug())) {
                throw new BadRequestException("Category with this name already exists");
            }
            category.setName(request.getName());
            category.setSlug(newSlug);
        }
        
        if (request.getDescription() != null) category.setDescription(request.getDescription());
        if (request.getImage() != null) category.setImage(request.getImage());
        if (request.getParentId() != null) category.setParentId(request.getParentId());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setActive(request.isActive());
        
        category = categoryRepository.save(category);
        return CategoryResponse.fromCategory(category);
    }
    
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        // Check if category has products
        if (!productRepository.findByCategoryId(categoryId).isEmpty()) {
            throw new BadRequestException("Cannot delete category with associated products");
        }
        
        // Check if category has sub-categories
        if (!categoryRepository.findByParentIdAndActiveTrue(categoryId).isEmpty()) {
            throw new BadRequestException("Cannot delete category with sub-categories");
        }
        
        category.setActive(false);
        categoryRepository.save(category);
    }
    
    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }
}
