package com.springboot.blog.service;

import com.springboot.blog.payload.AuthorDto;

import java.util.List;

public interface AuthorService {
    AuthorDto getAuthorById(Long id);
    AuthorDto getAuthorsByBookId (Long productId);
    List<AuthorDto> getAuthors();
    AuthorDto addAuthor(AuthorDto authorDto);
    AuthorDto updateAuthor(AuthorDto authorDto, Long id);
    void deleteAuthor(Long id);
    List<AuthorDto> addBatch(List<AuthorDto> authorsDto);
    List<AuthorDto> updateBatch(List<AuthorDto> authorsDto);
    void deleteBatch(List<Long> authorsId);
}
