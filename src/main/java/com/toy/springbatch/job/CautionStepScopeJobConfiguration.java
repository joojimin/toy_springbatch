package com.toy.springbatch.job;

import com.toy.springbatch.domain.User;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CautionStepScopeJobConfiguration {

    private final DataSource dataSource;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job cautionStepScopeJob() throws Exception {
        return jobBuilderFactory.get("cautionStepScopeJob")
                                .start(cautionStep())
                                .build();
    }


    @Bean
    public Step cautionStep() throws Exception {
        return stepBuilderFactory.get("cautionStep")
                                 .<User, User>chunk(2)
                                 .reader(itemReader())
                                 .writer(itemWriter())
                                 .build();
    }

    @Bean
    public ItemReader<User> itemReader() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("lastName", "joo");

        return new JdbcPagingItemReaderBuilder<User>()
            .pageSize(2)
            .fetchSize(2)
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(User.class))
            .queryProvider(createQueryProvider())
            .parameterValues(params)
            .name("jdbcPagingItemReader")
            .build();
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource); // Database에 맞는 PagingQueryProvider를 선택하기 위해
        queryProvider.setSelectClause("id, first_name, last_name, register_date, update_date");
        queryProvider.setFromClause("from user");
        queryProvider.setWhereClause("where last_name = :lastName");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<User> itemWriter() {
        return list -> list.forEach(user -> log.info("User ===> {}", user));
    }
}
