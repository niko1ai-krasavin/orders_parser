package com.example.orders_parser.writers;

import com.example.orders_parser.OrdersParserApplication;
import com.example.orders_parser.entities.Order;

import org.springframework.batch.item.ItemWriter;

import java.util.List;


public class CustomItemWriter implements ItemWriter<Order> {

    private static Order currentOrder;
    private static long currentId;

    @Override
    public synchronized void write(List<? extends Order> items) throws Exception {

        long id = 0;

        for (Order order : items) {
            currentOrder = order;
            currentId = order.getId();
            setOrderIntoMap();
        }
    }

    private boolean isValueFreeInMap(Long id) {
        if(OrdersParserApplication.mapOrders.get(id) == null) return true;
        return false;
    }

    private void setOrderIntoMap() {

        /**
         * If the record IDs of two files overlap,
         * the ID of the current record will be replaced with a free one from the map.
         */

        if(isValueFreeInMap(currentId)) {
            currentOrder.setId(currentId);
            OrdersParserApplication.mapOrders.put(currentId, currentOrder);
        } else {
            currentId++;
            setOrderIntoMap();
        }
    }
}
