package com.springboot.blog.controller;

import com.springboot.blog.payload.ProductDto;
import com.springboot.blog.payload.ProductResponse;
import com.springboot.blog.service.ProductService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/products")
@Tag(
        name = "CRUD REST APIs for Product Resource"
)
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Create Product REST API",
            description = "Create Product REST API is used to save product into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ProductResponse searchProducts(@RequestParam("query") String query,
                                                           @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                           @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return productService.searchProducts(query, pageNo, pageSize, sortBy, sortDir);
    }

    @Operation(
            summary = "Get All Products REST API",
            description = "Get All Products REST API is used to fetch all the products from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    // get all posts rest api
    @GetMapping
    public ProductResponse getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return productService.getAllProducts(pageNo, pageSize, sortBy, sortDir);
    }

    @Operation(
            summary = "Get Product By Id REST API",
            description = "Get Product By Id REST API is used to get a single product from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    //build get post by id
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

//    @GetMapping("/categories/{id}")
//    public ResponseEntity<List<ProductDto>> getAllProductsByCategory (@PathVariable("id") Long bookCategoryId) {
//        List<ProductDto> products = productService.getAllProductsByCategory(bookCategoryId);
//
//        return ResponseEntity.ok(products);
//    }

    @GetMapping("/categories/{id}")
    public ProductResponse getAllProductsByCategory (@PathVariable("id") Long bookCategoryId,
                                                     @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                     @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                     @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return productService.getProductsByCategoryId(bookCategoryId, pageNo, pageSize, sortBy, sortDir);
    }

    @Operation(
            summary = "Delete Product REST API",
            description = "Delete Product REST API is used to delete a particular product from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "id") Long id){

        productService.deleteProductById(id);

        return new ResponseEntity<>("Product entity deleted successfully.", HttpStatus.OK);
    }

}
