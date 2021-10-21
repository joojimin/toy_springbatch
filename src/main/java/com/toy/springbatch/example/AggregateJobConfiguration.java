package com.toy.springbatch.example;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

@Profile("example")
@RequiredArgsConstructor
@Configuration
public class AggregateJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;
    private final int chunkSize = 10;

    @Bean
    public Job aggregateJob() {
        return jobBuilderFactory.get("aggregateJob")
                                .start(aggregateStep(null))
                                .build();
    }

    @Bean
    @JobScope
    public Step aggregateStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("aggregateStep")
                .<Orders, AggregateMonth>chunk(chunkSize)
                .reader(itemReader())
                .processor(itemProcessor(null))
                .writer(itemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Orders> itemReader() {
        return new JpaPagingItemReaderBuilder<Orders>()
                .queryString("SELECT o FROM Orders o")
                .pageSize(chunkSize)
                .entityManagerFactory(entityManagerFactory)
                .name("ordersItemReader")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Orders, AggregateMonth> itemProcessor(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return orders -> AggregateMonth.create(LocalDateTime.parse(requestDate), orders.getTotalPrice(), 1L);
    }

    @Bean
    public JpaItemWriter<AggregateMonth> itemWriter() {
        JpaItemWriter writer = new JpaItemWriter();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
