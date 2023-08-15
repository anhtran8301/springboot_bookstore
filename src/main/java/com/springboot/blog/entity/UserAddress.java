package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_addresses")
public class UserAddress {
    @Id
    private Long id;

    private String address;
    private boolean isDefault;
    private int province;
    private int district;
    private int ward;
    private String phone;
    private String receiver;
    private String displayAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "userAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;
}
