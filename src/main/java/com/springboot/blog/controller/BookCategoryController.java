package com.springboot.blog.controller;

import com.springboot.blog.payload.BookCategoryDto;
import com.springboot.blog.service.BookCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/books-categories")
@Tag(
        name = "CRUD REST APIs for Book Category Resource"
)
public class BookCategoryController {
    private BookCategoryService bookCategoryService;

    public BookCategoryController(BookCategoryService bookCategoryService) {
        this.bookCategoryService = bookCategoryService;
    }

    @Operation(
            summary = "Get All Books Categories REST API",
            description = "Get All Books Categories REST API is used to fetch all the books categories from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping
    public ResponseEntity<List<BookCategoryDto>> getAllBooksCategories() {
        return ResponseEntity.ok(bookCategoryService.getAllBookCategories());
    }

    @Operation(
            summary = "Create Book Category REST API",
            description = "Create Book Category REST API is used to save book category into database"
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
    public ResponseEntity<BookCategoryDto> addBookCategory(@RequestBody BookCategoryDto bookCategoryDto) {
        BookCategoryDto savedBookCategory = bookCategoryService.addBookCategory(bookCategoryDto);
        return new ResponseEntity<>(savedBookCategory, HttpStatus.CREATED);
    }
}
