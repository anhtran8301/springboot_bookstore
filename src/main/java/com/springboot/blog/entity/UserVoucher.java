package com.springboot.blog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springboot.blog.entity.composite.OrderPKId;
import com.springboot.blog.entity.composite.UserVoucherPKId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(UserVoucherPKId.class)
@Table(name = "users_vouchers")
public class UserVoucher {
    @Id
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "voucher_id")
    @JsonBackReference
    private Voucher voucher;

    private Date earnedDate;
    private boolean isUsed;


}
