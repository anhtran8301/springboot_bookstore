package com.springboot.blog.service;

import com.springboot.blog.entity.Product;
import com.springboot.blog.payload.ProductDto;

import java.util.List;

public interface ProductService {
    List<Product> searchProducts(String query);

    ProductDto createProduct(ProductDto productDto);
}
