package com.springboot.blog.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private Long id;

    private String status;

    @CreationTimestamp
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"name", "email", "avatar"})
//    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    private String payType;
    private boolean isPay;
    private String shipName;
    private String shipNote;
    private String shipPhone;
    private String displayAddress;
    private double totalPrice;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnoreProperties({"id"})
    public List<OrderDetail> ordersDetails;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Transaction> transactions;


//    @JsonView(ViewJson.Base.class)
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @Transient
//    private Long userId;
//
//    public Long getUserId() {
//        return user.getId();
//    }

}
