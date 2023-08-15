package com.springboot.blog.payload.SaleDots;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SaleReq {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean IsActive;
    private List<SaleProductReq> saleProducts;

}
