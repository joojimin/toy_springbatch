package com.toy.springbatch.job;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomExitCodeJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job customExitCodeJob() {
        return jobBuilderFactory.get("customExitCodeJob")
                                .start(customStep1())
                                    .on("FAILED")
                                    .end()
                                .from(customStep1())
                                    .on("COMPLETED WITH SKIPS")
                                    .to(customErrorPrint())
                                .from(customStep1())
                                    .on("*")
                                    .to(customStep2())
                                .end()
                                .build();
    }


    @Bean
    public Step customStep1() {
        return stepBuilderFactory.get("customStep1")
                                 .tasklet((contribution, chunkContext) -> {
                                     log.info(">>>>>> customExitCodeJob step1");

                                     contribution.setExitStatus(ExitStatus.NOOP);

                                     return RepeatStatus.FINISHED;
                                 })
                                 .build();
    }

    @Bean
    public Step customErrorPrint() {
        return stepBuilderFactory.get("customErrorPrint")
                                 .tasklet((contribution, chunkContext) -> {
                                     log.error("errrrrrrrrrrrrrrrrrrrror");
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }


    @Bean
    public Step customStep2() {
        return stepBuilderFactory.get("customStep2")
                                 .tasklet((contribution, chunkContext) -> {
                                     log.info(">>>>>>>>>> customExitCodeJob step2");
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }
}
