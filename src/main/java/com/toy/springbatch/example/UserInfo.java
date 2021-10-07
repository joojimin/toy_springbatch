package com.toy.springbatch.example;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

@Profile("example")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserInfo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    public UserInfo(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
