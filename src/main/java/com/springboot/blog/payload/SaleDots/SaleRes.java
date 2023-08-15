package com.springboot.blog.payload.SaleDots;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SaleRes {
    private Long id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private List<SaleProductRes> saleProducts;
}
