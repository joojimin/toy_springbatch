package com.toy.springbatch.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.context.annotation.Profile;

@Profile("example")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Orders {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @OneToMany(mappedBy = "orders")
    private List<OrderItems> orderItems = new ArrayList<>();

    private LocalDateTime registerTime;
    private LocalDateTime updatedTime;

    private Orders(UserInfo userInfo, List<OrderItems> orderItems, LocalDateTime registerTime) {
        this.userInfo = userInfo;
        this.orderItems = orderItems;
        this.registerTime = registerTime;
    }

    public static Orders newInstance(UserInfo userInfo, List<OrderItems> orderItems) {
        return new Orders(userInfo, orderItems, LocalDateTime.now());
    }

    public Long getTotalPrice() {
        return orderItems.stream()
                .mapToLong(orderitem -> orderitem.getPrice())
                .sum();
    }
}
