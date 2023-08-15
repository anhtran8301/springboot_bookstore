package com.springboot.blog.service;

import com.springboot.blog.payload.ProductDto;
import com.springboot.blog.payload.ProductRequest;
import com.springboot.blog.payload.ProductResponse;

public interface ProductService {
//    List<ProductDto> searchProducts(String query);

    ProductDto createProduct(ProductRequest request);

    ProductResponse getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);

//    List<ProductDto> getAllProductsByCategory(Long bookCategory);

    ProductDto getProductById(Long bookCategoryId);

    void deleteProductById(Long id);

    ProductDto updateProduct(ProductRequest request, Long id);

    ProductResponse getProductsByCategoryId(Long bookCategory, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getProductsByAuthorId(Long authorId, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getProductsByPublisherId(Long publisherId, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse searchProducts(String query, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getBestSeller(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getNewest(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getBestDiscount(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getUpComing(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getRelatedBooks(Long id, int pageNo, int pageSize, String sortBy, String sortDir);

}
