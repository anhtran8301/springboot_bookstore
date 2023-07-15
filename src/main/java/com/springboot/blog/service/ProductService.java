package com.springboot.blog.service;

import com.springboot.blog.payload.ProductDto;
import com.springboot.blog.payload.ProductResponse;

public interface ProductService {
//    List<ProductDto> searchProducts(String query);

    ProductDto createProduct(ProductDto productDto);

    ProductResponse getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);

//    List<ProductDto> getAllProductsByCategory(Long bookCategory);

    ProductDto getProductById(Long bookCategoryId);

    void deleteProductById(Long id);

    ProductDto updateProduct(ProductDto productDto, Long id);

    //
    ProductResponse getProductsByCategoryId(Long bookCategory, int pageNo, int pageSize, String sortBy, String sortDir);

    ProductResponse searchProducts(String query, int pageNo, int pageSize, String sortBy, String sortDir);
}
