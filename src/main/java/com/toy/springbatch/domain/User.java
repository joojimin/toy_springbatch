package com.toy.springbatch.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    private LocalDateTime registerDate;
    private LocalDateTime updateDate;

    @Builder
    public User(String firstName, String lastName, LocalDateTime registerDate,
                LocalDateTime updateDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }
}
