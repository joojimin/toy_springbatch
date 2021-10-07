package com.toy.springbatch.example;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("example")
@RequiredArgsConstructor
@Configuration
public class AggregateJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job aggregateJob() {
        return jobBuilderFactory.get("aggregateJob")
                                .start(aggregateStep())
                                .build();
    }

    @Bean
    public Step aggregateStep() {
        return stepBuilderFactory.get("aggregateStep")
                                 .tasklet((contribution, chunkContext) -> {
                                     System.out.println("hello world!");
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }
}
