package com.toy.springbatch.example;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AggregateMonth {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime aggregateMonth;
    private Long totalSalesAmount;

    private Long jobExecutionId;
    private LocalDateTime registerTime;

    @Builder
    private AggregateMonth(LocalDateTime aggregateMonth, Long totalSalesAmount, Long jobExecutionId, LocalDateTime registerTime) {
        this.aggregateMonth = aggregateMonth;
        this.totalSalesAmount = totalSalesAmount;
        this.jobExecutionId = jobExecutionId;
        this.registerTime = registerTime;
    }

    public static AggregateMonth create(LocalDateTime aggregateMonth, Long totalSalesAmount, Long jobExecutionId) {
        return new AggregateMonth(aggregateMonth, totalSalesAmount, jobExecutionId, LocalDateTime.now());
    }
}
