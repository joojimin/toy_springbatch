package com.toy.springbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleChunkJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleChunkJob() {
        return jobBuilderFactory.get("simpleChunkJob")
                .start(simpleChunkStep())
                .build();
    }

    @Bean
    public Step simpleChunkStep() {
        return stepBuilderFactory.get("simpleChunkStep")
                .<Integer, String>chunk(20)
                .reader(flatFileReader())
                .processor(simpleChunkProcessor())
                .writer(simpleChunkWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<Integer> flatFileReader() {
        return new FlatFileItemReaderBuilder<Integer>()
                .name("flatFileReader")
                .resource(new FileSystemResource("./test.txt"))
                .targetType(Integer.class)
                .build();
    }

    @Bean
    public ItemProcessor<Integer, String> simpleChunkProcessor() {
        return num -> String.format("accept processor => %d", num);
    }

    @Bean
    public ItemWriter<String> simpleChunkWriter() {
        return items -> log.info(String.join(",", items));
    }

}
