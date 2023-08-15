package com.springboot.blog.controller;

import com.springboot.blog.payload.AuthorDto;
import com.springboot.blog.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/authors")
@Tag(
        name = "CRUD REST APIs for Author Resource"
)
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAuthors(){
        return ResponseEntity.ok(authorService.getAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Operation(
            summary = "Create Author REST API",
            description = "Create Author REST API is used to save author into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AuthorDto> addAuthor(@Valid @RequestBody AuthorDto authorDto){
        return new ResponseEntity<>(authorService.addAuthor(authorDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Author REST API",
            description = "Update Author REST API is used to update a particular author in the database"
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
    public ResponseEntity<AuthorDto> updateAuthor(@Valid @RequestBody AuthorDto authorDto, @PathVariable(name = "id") Long id){
        return new ResponseEntity<>(authorService.updateAuthor(authorDto, id), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Author REST API",
            description = "Delete Author REST API is used to delete a particular author from the database"
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
    public ResponseEntity<String> deleteAuthor(@PathVariable(name = "id") long id){
        authorService.deleteAuthor(id);
        return new ResponseEntity<>("Author entity deleted successfully.", HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/batch")
    public ResponseEntity<List<AuthorDto>> addBatch(@Valid @RequestBody List<AuthorDto> authors){
        return new ResponseEntity<>(authorService.addBatch(authors), HttpStatus.CREATED);

    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/batch")
    public ResponseEntity<List<AuthorDto>> updateBatch(@Valid @RequestBody List<AuthorDto> authors){
        return new ResponseEntity<>(authorService.updateBatch(authors), HttpStatus.OK);

    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/batch")
    public ResponseEntity<String> deleteBatch(@Valid @RequestBody List<Long> authorsId){
        authorService.deleteBatch(authorsId);
        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
    }
}
