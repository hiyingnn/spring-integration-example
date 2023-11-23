package com.example.springintegration.config;

import com.example.springintegration.mapper.OrderMapper;
import com.example.springintegration.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class JobConfig {

    @Bean
    public Job readLogOrderJob(Step readLogOrderStep, JobRepository jobRepository){
        return new JobBuilder("readLogOrderJob", jobRepository)
                .start(readLogOrderStep)
                .build();
    }

    @Bean
    public Step readLogOrderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemReader<Order> orderItemReader) {
        return new StepBuilder("readLogOrderStep", jobRepository)
                .allowStartIfComplete(true)
                .<Order, Order>chunk(1000, transactionManager)
                .reader(orderItemReader)
                .processor((ItemProcessor<Order, Order>) order -> {
                    // log out the order
                    log.info(order.toString());
                    return order;
                })
                .writer(chunk -> {
                    // do nothing
                }).build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader sampleReader(@Value("#{jobParameters['input.file.name']}") String resource) {

        FlatFileItemReader<Order> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource(resource));


        DefaultLineMapper<Order> orderLineMapper = new DefaultLineMapper<>();
        orderLineMapper.setFieldSetMapper(new OrderMapper());
        DelimitedLineTokenizer tokenizerDet = new DelimitedLineTokenizer(",");
        tokenizerDet.setNames("referenceCode", "name", "firstUpdated", "lastUpdated");
        orderLineMapper.setLineTokenizer(tokenizerDet);

        flatFileItemReader.setLineMapper(orderLineMapper);
        return flatFileItemReader;
    }

}
