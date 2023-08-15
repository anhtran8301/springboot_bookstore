package com.springboot.blog.service.impl;

import com.github.slugify.Slugify;
import com.springboot.blog.entity.*;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.ProductDto;
import com.springboot.blog.payload.ProductRequest;
import com.springboot.blog.payload.ProductResponse;
import com.springboot.blog.repository.*;
import com.springboot.blog.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final BookCategoryRepository bookCategoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final SaleRepository saleRepository;
    private final SaleProductRepository saleProductRepository;
//    @Override
//    public List<ProductDto> searchProducts(String query) {
//        if (query == null) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Keyword do not empty or blank");
//        }
//        List<Product> products = productRepository.searchProducts(query);
//        return products.stream().map((product -> modelMapper.map(product, ProductDto.class)))
//                .collect(Collectors.toList());
//    }

    @Override
    public ProductResponse searchProducts(String query, int pageNo, int pageSize, String sortBy, String sortDir) {
        if (query == null) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Keyword do not empty or blank");
        }
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.searchProductsPaginate(query, pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductDto createProduct(ProductRequest request) {
        BookCategory bookCategory = bookCategoryRepository.findById(request.getBookCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Book Category", "id", request.getBookCategoryId()));

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", request.getAuthorId()));

        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "id", request.getPublisherId()));

        if (productRepository.existsByName(request.getName())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "This name is already exists");
        }
        Product product = modelMapper.map(request, Product.class);
        if(request.getSlug() == null) {
            var slg = Slugify.builder().build();
            product.setSlug(slg.slugify(request.getName()));
        } else {
            product.setSlug(request.getSlug());
        }

        if(productRepository.existsBySlug(product.getSlug())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "The slug is exist in db");
        }

        product.setBookCategory(bookCategory);
        product.setAuthor(author);
        product.setPublisher(publisher);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductResponse getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findAll(pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

//    @Override
//    public List<ProductDto> getAllProductsByCategory(Long bookCategoryId) {
//        BookCategory bookCategory = bookCategoryRepository.findById(bookCategoryId)
//                .orElseThrow(() -> new ResourceNotFoundException("Book Category", "id", bookCategoryId));
//
//
//        List<Product> products = productRepository.findByBookCategoryId(bookCategoryId);
//
//        return products.stream().map((product) -> mapToDTO(product)).collect(Collectors.toList());
//    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapToDTO(product);
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
    }

    @Override
    public ProductDto updateProduct(ProductRequest request, Long id) {
        var product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id));

        var bookCategory = bookCategoryRepository.findById(request.getBookCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Book Category", "id", request.getBookCategoryId()));

        var author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", request.getAuthorId()));

        var publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "id", request.getPublisherId()));

        if (!request.getName().equals(product.getName()) && productRepository.existsByName(request.getName())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "The name is exist in db");
        }

        if(request.getSlug() == null){
            var slg = Slugify.builder().build();
            product.setSlug(slg.slugify(request.getName()));
        } else {
            product.setSlug(request.getSlug());
        }

        if(productRepository.existsBySlug(product.getSlug())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "The slug is exist in db");
        }

//        product.setSku(productDto.getSku());
//        product.setName(productDto.getName());
//        product.setDescription(productDto.getDescription());
//        product.setActive(productDto.isActive());
//        product.setImagesString(productDto.getImagesString());
//        product.setUnitPrice(productDto.getUnitPrice());
//        product.setReleaseDate(productDto.getReleaseDate());
//        product.setSold(productDto.getSold());
//        product.setDiscount(productDto.getDiscount());
//        product.getBookCategory().setId(productDto.getBookCategoryId());
//        product.getAuthor().setId(productDto.getAuthorId());
//        product.getPublisher().setId(productDto.getPublisherId());


        productRepository.save(modelMapper.map(request, Product.class));
        return modelMapper.map(product, ProductDto.class);
    }

    private ProductDto mapToDTO(Product product) {
        //using ModelMapper
        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        return productDto;
    }

    //test
    @Override
    public ProductResponse getProductsByCategoryId(Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir) {
        BookCategory bookCategory = bookCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Book Category", "id", categoryId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findByBookCategoryId(categoryId, pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByAuthorId(Long authorId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", authorId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findByAuthorId(authorId, pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    public ProductResponse getProductsByPublisherId(Long publisherId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "id", publisherId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findByPublisherId(publisherId, pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getBestSeller(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findBestSeller(pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getNewest(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findNewest(pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getBestDiscount(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findBestDiscount(pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getUpComing(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findUpComing(pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getRelatedBooks(Long id, int pageNo, int pageSize, String sortBy, String sortDir) {
        var product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id)
        );

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findRelatedBooks(pageable, id, product.getBookCategory().getId());

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(x -> mapToDTO(x)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    public List<ProductDto> getBookRes(List<Product> products) {
        var sale = saleRepository.getCurrentSales();
        var saleProducts = saleProductRepository.findAll();


    }
}

