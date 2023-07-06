package com.springboot.blog.service.impl;

import com.springboot.blog.entity.BookCategory;
import com.springboot.blog.entity.Product;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.ProductDto;
import com.springboot.blog.payload.ProductResponse;
import com.springboot.blog.repository.BookCategoryRepository;
import com.springboot.blog.repository.ProductRepository;
import com.springboot.blog.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    private BookCategoryRepository bookCategoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ModelMapper modelMapper,
                              BookCategoryRepository bookCategoryRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.bookCategoryRepository = bookCategoryRepository;
    }

//    @Override
//    public List<ProductDto> searchProducts(String query) {
//        if (query == null) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Keyword do not empty or blank");
//        }
//        List<Product> products = productRepository.searchProducts(query);
//        return products.stream().map((product -> modelMapper.map(product, ProductDto.class)))
//                .collect(Collectors.toList());
//    }

    @Override
    public ProductResponse searchProducts(String query, int pageNo, int pageSize, String sortBy, String sortDir) {
        if (query == null) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Keyword do not empty or blank");
        }
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.searchProductsPaginate(query, pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        BookCategory bookCategory = bookCategoryRepository.findById(productDto.getBookCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Book Category", "id", productDto.getBookCategoryId()));

        if (productRepository.existsByName(productDto.getName())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "This name is already exists");
        }
        Product product = modelMapper.map(productDto, Product.class);
        product.setBookCategory(bookCategory);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductResponse getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findAll(pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

//    @Override
//    public List<ProductDto> getAllProductsByCategory(Long bookCategoryId) {
//        BookCategory bookCategory = bookCategoryRepository.findById(bookCategoryId)
//                .orElseThrow(() -> new ResourceNotFoundException("Book Category", "id", bookCategoryId));
//
//
//        List<Product> products = productRepository.findByBookCategoryId(bookCategoryId);
//
//        return products.stream().map((product) -> mapToDTO(product)).collect(Collectors.toList());
//    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapToDTO(product);
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productRepository.delete(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Long id) {
        return null;
    }

    private ProductDto mapToDTO(Product product) {
        //using ModelMapper
        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        return productDto;
    }
//test
    @Override
    public ProductResponse getProductsByCategoryId(Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir) {
        BookCategory bookCategory = bookCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Book Category", "id", categoryId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findByBookCategoryId(categoryId, pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }
}
