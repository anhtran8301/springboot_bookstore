package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Author;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.AuthorDto;
import com.springboot.blog.repository.AuthorRepository;
import com.springboot.blog.service.AuthorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper mapper;

    @Override
    public AuthorDto getAuthorById(Long id){
        var author = authorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Author", "id", id));

        return mapper.map(author, AuthorDto.class);
    }
    @Override
    public AuthorDto getAuthorsByBookId (Long productId){
       var authors = authorRepository.findAll()
               .stream()
               .filter(author -> author.getProducts().stream().anyMatch(product -> Objects.equals(product.getId(), productId)))
               .toList();

        if (!authors.isEmpty()) {
            return mapper.map(authors.get(0), AuthorDto.class);
        }
        return null;

    }
    @Override
    public List<AuthorDto> getAuthors(){
        var authors = authorRepository.findAll();
        return authors.stream().map(author -> mapper.map(author, AuthorDto.class)).toList();
    }
    @Override
    public AuthorDto addAuthor(AuthorDto authorDto){
        var author = mapper.map(authorDto, Author.class);
        authorRepository.save(author);
        return mapper.map(author, AuthorDto.class);
    }
    @Override
    public AuthorDto updateAuthor(AuthorDto authorDto, Long id){
        var author = authorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Author", "id", id));

        if(authorDto.getName().equals(author.getName())
            && authorDto.getDescription().equals(author.getDescription())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Nothing to change");
        }

        author.setName(authorDto.getName());
        author.setDescription(authorDto.getDescription());
        authorRepository.save(author);

        return mapper.map(author, AuthorDto.class);
    }
    @Override
    public void deleteAuthor(Long id){
        var author = authorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Author", "id", id));
        authorRepository.delete(author);
    }
    @Override
    public List<AuthorDto> addBatch(List<AuthorDto> authorsDto){
        var authorsEntity = authorsDto.stream()
                .map(dto -> mapper.map(dto, Author.class))
                .collect(Collectors.toList());

        var result = authorRepository.saveAll(authorsEntity);

        return result.stream()
                .map(author -> mapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public List<AuthorDto> updateBatch(List<AuthorDto> request) {
        var ids = request.stream().map(AuthorDto::getId).collect(Collectors.toList());
        var authors = authorRepository.findByIds(ids);
        if(authors.size() != ids.size()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Not found with some author id");
        }
        for (var author: request){
            authorRepository.updateBatch(author.getId(), author.getName(), author.getDescription());
        }

        return request;
    }
    @Override
    public void deleteBatch(List<Long> authorsId) {
        List<Author> authorsEntity = authorRepository.findAll();
        List<Author> result = authorsEntity.stream()
                .filter(u -> authorsId.contains(u.getId()))
                .collect(Collectors.toList());

        authorRepository.deleteAllInBatch(result);
    }
}
