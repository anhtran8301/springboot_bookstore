package com.springboot.blog.controller;

import com.springboot.blog.entity.Category;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
@Tag(
        name = "CRUD REST APIs for Category Resource"
)
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //build add category REST API
    @Operation(
            summary = "Create Category REST API",
            description = "Create Category REST API is used to save category into database"
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
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Category By Id REST API",
            description = "Get Category By Id REST API is used to get a single category from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    //build get category REST API
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") Long categoryId) {
        CategoryDto categoryResponse = categoryService.getCategory(categoryId);

        return ResponseEntity.ok(categoryResponse);
    }

    //build get all categories REST API
    @Operation(
            summary = "Get All Categories REST API",
            description = "Get All Categories REST API is used to fetch all the categories from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {

        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    //build update category REST API
    @Operation(
            summary = "Update Category REST API",
            description = "Update Category REST API is used to update a particular category in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
                                                      @PathVariable("id") Long categoryId) {

        return ResponseEntity.ok(categoryService.updateCategory(categoryDto, categoryId));

    }

    //build delete category REST API
    @Operation(
            summary = "Delete Category REST API",
            description = "Delete Category REST API is used to delete a particular category from the database"
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
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId) {

        categoryService.deleteCategory(categoryId);

        return ResponseEntity.ok("Category deleted successfully");
    }
}
