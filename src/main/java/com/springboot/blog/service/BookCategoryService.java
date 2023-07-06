package com.springboot.blog.service;

import com.springboot.blog.payload.BookCategoryDto;

import java.util.List;

public interface BookCategoryService {
    BookCategoryDto addBookCategory(BookCategoryDto bookCategoryDto);

    List<BookCategoryDto> getAllBookCategories();

//    Boolean existsByName(String name);

}
