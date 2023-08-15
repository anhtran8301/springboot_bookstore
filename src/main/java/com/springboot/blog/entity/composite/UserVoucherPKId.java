package com.springboot.blog.entity.composite;

import com.springboot.blog.entity.User;
import com.springboot.blog.entity.Voucher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserVoucherPKId implements Serializable {
    private User user;
    private Voucher voucher;
}
