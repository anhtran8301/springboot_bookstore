package com.springboot.blog.service;

import com.springboot.blog.payload.SaleDots.SaleReq;
import com.springboot.blog.payload.SaleDots.SaleRes;

import java.util.List;

public interface SaleService {
    List<SaleRes> getSales();
    SaleRes getCurrentSales();
    SaleRes getSaleById(Long id);
    SaleRes addSale(SaleReq request);
    SaleRes updateSale(SaleReq request);
    void deleteSale(Long id);
}
