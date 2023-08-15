package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Sale;
import com.springboot.blog.entity.SaleProduct;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.ProductDto;
import com.springboot.blog.payload.SaleDots.SaleProductRes;
import com.springboot.blog.payload.SaleDots.SaleReq;
import com.springboot.blog.payload.SaleDots.SaleRes;
import com.springboot.blog.repository.ProductRepository;
import com.springboot.blog.repository.SaleProductRepository;
import com.springboot.blog.repository.SaleRepository;
import com.springboot.blog.service.SaleService;
import com.springboot.blog.utils.AppConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final ModelMapper mapper;
    private final ProductRepository productRepository;
    private final SaleProductRepository saleProductRepository;

    @Override
    public List<SaleRes> getSales(){
        var sales = saleRepository.findAll();
        return getSaleRes(sales);
    }

    @Override
    public SaleRes getCurrentSales(){
        var sales = saleRepository.getCurrentSales();

        return getSaleRes(sales).stream()
                .findFirst()
                .orElseThrow(
                        () -> new BlogAPIException(HttpStatus.BAD_REQUEST, AppConstants.FLASH_SALE_NOT_TODAY)
                );
    }

    @Override
    public SaleRes getSaleById(Long id){
        var sale = saleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Sale", "id", id));
        List<Sale> sales = new ArrayList<>();
        sales.add(sale);
        return getSaleRes(sales).stream()
                .findFirst().orElseThrow(
                        () -> new BlogAPIException(HttpStatus.BAD_REQUEST, AppConstants.FLASH_SALE_NOT_EXIST)
                );
    }

    private List<SaleRes> getSaleRes(List<Sale> sales){
        var saleProducts = saleProductRepository.findAll();
        var products = productRepository.findAll();

        var saleResList = sales.stream().map(sale -> {
            SaleRes saleRes = new SaleRes();
            saleRes.setId(sale.getId());
            saleRes.setName(sale.getName());
            saleRes.setDescription(sale.getDescription());
            saleRes.setStartDate(sale.getStartDate());
            saleRes.setEndDate(sale.getEndDate());
            saleRes.setActive(sale.isActive());
            var saleProductResList = saleProducts.stream()
                    .filter(saleProduct -> Objects.equals(saleProduct.getSale().getId(), sale.getId()))
                    .map(saleProduct -> {
                        SaleProductRes saleProductRes = new SaleProductRes();
                        saleProductRes.setProductId(saleProduct.getProduct().getId());
                        saleProductRes.setDiscount(saleProduct.getDiscount());
                        saleProductRes.setQuantity(saleProduct.getQuantity());
                        saleProductRes.setProduct(mapper.map(saleProduct.getProduct(), ProductDto.class));

                        return saleProductRes;
                    }).toList();

            saleRes.setSaleProducts(saleProductResList);
            return saleRes;
        }).toList();

        return saleResList;
    }

    @Override
    @Transactional
    @Async
    public SaleRes addSale(SaleReq request){

        if (request.getEndDate().isBefore(LocalDateTime.now()))
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, AppConstants.FLASH_SALE_INVALID_END_TIME);

        var sameTimeSale = saleRepository.sameTimeSale(request.getStartDate(), request.getEndDate());
        if(sameTimeSale != null)
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, AppConstants.FLASH_SALE_SAME_TIME);

        var sale = mapper.map(request, Sale.class);

        List<SaleProduct> saleProducts = request.getSaleProducts().stream()
                .map(productReq -> {
                    SaleProduct saleProduct = mapper.map(productReq, SaleProduct.class);
                    saleProduct.setSale(sale);
                    return saleProduct;
                }).toList();
//        sale.setSaleProducts(sale.getSaleProducts().stream()
//                .map(x -> {
//                    var saleProduct = new SaleProduct();
//                    saleProduct.setProduct(x.getProduct());
//                    saleProduct.setDiscount(x.getDiscount());
//                    saleProduct.setQuantity(x.getQuantity());
//                    System.out.println(saleProduct);
//                    return saleProduct;
//                }).collect(Collectors.toList())
//        );
//        var result = sale.getSaleProducts().stream()
//                .map(x -> {
//                    var saleProduct = new SaleProduct();
//                    saleProduct.setProduct(x.getProduct());
//                    saleProduct.setDiscount(x.getDiscount());
//                    saleProduct.setQuantity(x.getQuantity());
//                    System.out.println(saleProduct);
//                    return saleProduct;
//                }).collect(Collectors.toList());
        sale.setSaleProducts(saleProducts);
        saleRepository.save(sale);
        saleProductRepository.saveAll(sale.getSaleProducts());

        return mapper.map(sale, SaleRes.class);
    }

    @Override
    @Transactional
    public SaleRes updateSale(SaleReq request) {
        if (request.getEndDate().isBefore(LocalDateTime.now()))
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, AppConstants.FLASH_SALE_INVALID_END_TIME);

        Sale existingSale = saleRepository.findById(request.getId()).orElseThrow(
                () -> new BlogAPIException(HttpStatus.BAD_REQUEST, AppConstants.FLASH_SALE_NOT_EXIST)
        );

        Sale updatedSale = mapper.map(request, Sale.class);

        // Delete existing SaleBooks
        List<SaleProduct> existingSaleProducts = saleProductRepository.findBySaleId(existingSale.getId());
        saleProductRepository.deleteAll(existingSaleProducts);

        // Save updated SaleBooks
        List<SaleProduct> saleProducts = request.getSaleProducts().stream()
                .map(productReq -> {
                    SaleProduct saleProduct = mapper.map(productReq, SaleProduct.class);
                    saleProduct.setSale(updatedSale);
                    return saleProduct;
                })
                .collect(Collectors.toList());

        updatedSale.setSaleProducts(saleProducts);

        saleRepository.save(updatedSale);
        saleProductRepository.saveAll(updatedSale.getSaleProducts());
        SaleRes result = mapper.map(updatedSale, SaleRes.class);
        return result;
    }

    @Override
    public void deleteSale(Long id){
        var sale = saleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Sale", "id", id));
        saleRepository.delete(sale);
    }
}
