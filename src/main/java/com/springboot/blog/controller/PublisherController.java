package com.springboot.blog.controller;

import com.springboot.blog.payload.MessageResponse;
import com.springboot.blog.payload.PublisherDto;
import com.springboot.blog.service.PublisherService;
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
@RequestMapping("/api/v1/publishers")
@Tag(
        name = "CRUD REST APIs for Publisher Resource"
)
public class PublisherController {
    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<List<PublisherDto>> getPublishers(){
        return ResponseEntity.ok(publisherService.getPublishers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDto> getPublisherById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @Operation(
            summary = "Create Publisher REST API",
            description = "Create Publisher REST API is used to save publisher into database"
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
    public ResponseEntity<PublisherDto> addPublisher(@Valid @RequestBody PublisherDto publisherDto){
        return new ResponseEntity<>(publisherService.addPublisher(publisherDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Publisher REST API",
            description = "Update Publisher REST API is used to update a particular publisher in the database"
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
    public ResponseEntity<?> updatePublisher(@Valid @RequestBody PublisherDto publisherDto, @PathVariable(name = "id") Long id){
        try {
            return new ResponseEntity<>(publisherService.updatePublisher(publisherDto, id), HttpStatus.OK);

        } catch (Exception exception){
            return new ResponseEntity<>(new MessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Delete Publisher REST API",
            description = "Delete Publisher REST API is used to delete a particular publisher from the database"
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
    public ResponseEntity<String> deletePublisher(@PathVariable(name = "id") long id){
        publisherService.deletePublisher(id);
        return new ResponseEntity<>("Publisher entity deleted successfully.", HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/batch")
    public ResponseEntity<List<PublisherDto>> addBatch(@Valid @RequestBody List<PublisherDto> publishers){
        return new ResponseEntity<>(publisherService.addBatch(publishers), HttpStatus.CREATED);

    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/batch")
    public ResponseEntity<List<PublisherDto>> updateBatch(@Valid @RequestBody List<PublisherDto> publishers){
        return new ResponseEntity<>(publisherService.updateBatch(publishers), HttpStatus.OK);

    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/batch")
    public ResponseEntity<String> deleteBatch(@Valid @RequestBody List<Long> publishersId){
        publisherService.deleteBatch(publishersId);
        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
    }
}
