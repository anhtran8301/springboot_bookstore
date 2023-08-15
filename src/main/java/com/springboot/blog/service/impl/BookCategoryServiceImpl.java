package com.springboot.blog.service.impl;

import com.springboot.blog.entity.BookCategory;
import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.payload.BookCategoryDto;
import com.springboot.blog.repository.BookCategoryRepository;
import com.springboot.blog.security.CustomUserDetailsService;
import com.springboot.blog.service.BookCategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {

    private final CustomUserDetailsService userDetailsService;
    private final BookCategoryRepository bookCategoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public BookCategoryDto addBookCategory(BookCategoryDto bookCategoryDto) {
        User user = userDetailsService.getCurrentUser();
        if (bookCategoryRepository.existsByName(bookCategoryDto.getName())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "This name is already exists");
        }
        BookCategory bookCategory = modelMapper.map(bookCategoryDto, BookCategory.class);
        bookCategory.setUser(user);
        BookCategory savedBookCategory = bookCategoryRepository.save(bookCategory);

        return modelMapper.map(savedBookCategory, BookCategoryDto.class);
    }

    @Override
    public List<BookCategoryDto> getAllBookCategories() {
        List<BookCategory> bookCategories = bookCategoryRepository.findAll();

        return bookCategories.stream().map(bookCategory -> modelMapper.map(bookCategory, BookCategoryDto.class))
                .collect(Collectors.toList());
    }


}
