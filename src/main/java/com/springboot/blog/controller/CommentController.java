package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(
            summary = "Add Comment REST API",
            description = "API to add a Comment to a Post "
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId,
                                                    @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get All Comments REST API",
            description = "Get All Comments REST API is used to fetch all the comments from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getAllCommentByPostId(@PathVariable(value = "postId") Long postId) {
        return commentService.getAllCommentsByPostId(postId);
    }

    @Operation(
            summary = "Get Comment By Id REST API",
            description = "Get Comment By Id REST API is used to get a single comment from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") Long postId,
                                                     @PathVariable(value = "commentId") Long commentId) {
        CommentDto commentDto = commentService.getCommentById(postId, commentId);

        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Update Comment REST API",
            description = "Update Comment REST API is used to update a particular comment in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value = "commentId") long commentId,
                                                    @Valid @RequestBody CommentDto commentDto) {
        CommentDto commentResponse = commentService.updateComment(postId, commentId, commentDto);

        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Comment REST API",
            description = "Delete Comment REST API is used to delete a particular comment from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteCommentById(postId, commentId);

        return new ResponseEntity<>("Comment entity deleted successfully", HttpStatus.OK);
    }
}
