package com.toy.springbatch.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import java.util.Objects;

@Profile("example")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderItems {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public OrderItems(Orders orders, Item item) {
        this.orders = orders;
        this.item = item;
    }

    public Long getPrice() {
        if (Objects.isNull(item)) {
            return 0l;
        }
        return item.getPrice();
    }
}
