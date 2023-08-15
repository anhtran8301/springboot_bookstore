package com.springboot.blog.entity.composite;

import com.springboot.blog.entity.Product;
import com.springboot.blog.entity.Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SaleBookPKId implements Serializable {
    private Product product;
    private Sale sale;
}
