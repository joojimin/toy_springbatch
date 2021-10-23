package com.toy.springbatch.job.chunk;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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
                .<UserDto, String>chunk(5)
                .reader(flatFileReader())
                .processor(simpleChunkProcessor())
                .writer(simpleChunkWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<UserDto> flatFileReader() {
        return new FlatFileItemReaderBuilder<UserDto>()
                .name("flatFileReader")
                .resource(new ClassPathResource("test.txt"))
                .targetType(UserDto.class)
                .lineMapper(lineMapper())
                .build();
    }

    @Bean
    public LineMapper<UserDto> lineMapper() {
        DefaultLineMapper<UserDto> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("name");

        BeanWrapperFieldSetMapper<UserDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(UserDto.class);

        lineMapper.setLineTokenizer(lineTokenizer); // lineToken
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public ItemProcessor<UserDto, String> simpleChunkProcessor() {
        return userDto -> String.format("accept processor => %s", userDto.getName());
    }

    @Bean
    public ItemWriter<String> simpleChunkWriter() {
        return items -> System.out.println(String.join(", ", items));
    }

}
