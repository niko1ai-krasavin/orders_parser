package com.example.orders_parser.configurations;

import com.example.orders_parser.entities.Order;
import com.example.orders_parser.readers.CustomCsvFileItemReader;
import com.example.orders_parser.readers.CustomJsonFileItemReader;
import com.example.orders_parser.writers.CustomItemWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration()
@EnableBatchProcessing()
@ComponentScan({"com/example/orders_parser/readers", "com/example/orders_parser/writers"})
public class BatchConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean(name = "job")
    public Job job(Step stepParseCSV, Step stepParseJSON) {
        return jobBuilderFactory.get("jobBuilder")
                .start(stepParseCSV)
                .next(stepParseJSON)
                .build();
    }

    @Bean
    public Step stepParseCSV(ItemReader<Order> customCsvFileItemReader, ItemWriter<Order> customItemWriter) {
        return stepBuilderFactory.get("stepBuilderForFilesCSV")
                .<Order, Order>chunk(1)
                .reader(customCsvFileItemReader)
                .writer(customItemWriter)
                .build();
    }

    @Bean
    public Step stepParseJSON(ItemReader<Order> customJsonFileItemReader,ItemWriter<Order> customItemWriter) {
        return stepBuilderFactory.get("stepBuilderForFilesJSON")
                .<Order, Order> chunk(1)
                .reader(customJsonFileItemReader)
                .writer(customItemWriter)
                .build();
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
}
