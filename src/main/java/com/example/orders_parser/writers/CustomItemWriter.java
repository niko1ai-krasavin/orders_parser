package com.example.orders_parser.writers;

import com.example.orders_parser.entities.Order;

import org.springframework.batch.item.ItemWriter;

import java.util.List;


public class CustomItemWriter implements ItemWriter<Order> {
    @Override
    public void write(List<? extends Order> items) throws Exception {
        for (Order order : items) System.out.println(order);
    }
}
