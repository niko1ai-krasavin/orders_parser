package com.example.orders_parser.configurations;

import com.example.orders_parser.entities.Order;
import com.example.orders_parser.mappers.CustomOrderMapper;
import com.example.orders_parser.readers.CustomCsvFileItemReader;
import com.example.orders_parser.readers.CustomJsonFileItemReader;
import com.example.orders_parser.writers.CustomItemWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;


@Configuration()
@EnableBatchProcessing()
public class BatchConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean(name = "job")
    public Job job() {
        return jobBuilderFactory.get("jobBuilder")
                .start(splitFlow())
                .build()
                .build();
    }

    @Bean
    public Flow splitFlow() {
        return new FlowBuilder<SimpleFlow>("splitFlowBuilder")
                .split(taskExecutor())
                .add(flowParseCSV() ,flowParseJSON())
                .build();
    }

    @Bean
    public Flow flowParseCSV() {
        return new FlowBuilder<SimpleFlow>("flowParseCSV")
                .start(stepParseCSV())
                .build();
    }

    @Bean
    public Flow flowParseJSON() {
        return new FlowBuilder<SimpleFlow>("flowParseJSON")
                .start(stepParseJSON())
                .build();
    }

    @Bean
    public Step stepParseCSV() {
        return stepBuilderFactory.get("stepBuilderForFilesCSV")
                .<Order, Order>chunk(10)
                .reader(customCsvFileItemReader())
                .writer(customItemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step stepParseJSON() {
        return stepBuilderFactory.get("stepBuilderForFilesJSON")
                .<Order, Order> chunk(10)
                .reader(customJsonFileItemReader())
                .writer(customItemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean()
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean(name = "customCsvFileItemReader")
    public ItemReader<Order> customCsvFileItemReader() {
        return new CustomCsvFileItemReader();
    }

    @Bean(name = "customJsonFileItemReader")
    public ItemReader<Order> customJsonFileItemReader() {
        return new CustomJsonFileItemReader();
    }

    @Bean(name = "customItemWriter")
    public ItemWriter<Order> customItemWriter() {
        return new CustomItemWriter();
    }

    @Bean
    public CustomOrderMapper customOrderMapper() {return new CustomOrderMapper();}
}
