package com.example.orders_parser.mappers;

import com.example.orders_parser.entities.Order;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CustomOrderMapper {

    private String[] fieldSet;
    private long line;
    private String filename;
    private String result;

    public Order getOrder() {
        Order order = new Order();
        order.setResult(result);

        try {
            long l = Long.parseLong(fieldSet[0]);
            if(l <= 0) new NumberFormatException();
            order.setId(l);
        } catch (NumberFormatException e) {
            order.setResult("ERRORS: The line contains invalid data. " + e.getMessage());
        }
        try {
            double l = Double.parseDouble(fieldSet[1]);
            if(l <= 0) new NumberFormatException();
            order.setAmount(l);
        } catch (NumberFormatException e) {
            order.setResult("ERRORS: The line contains invalid data. " + e.getMessage());
        }

        order.setCurrency(fieldSet[2]);
        order.setComment(fieldSet[3]);
        order.setFilename(filename);
        order.setLine(line);

        return order;
    }
}
