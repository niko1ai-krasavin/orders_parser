package com.example.orders_parser.mappers;

import com.example.orders_parser.entities.Order;


public class CustomOrderMapper {

    private long line;
    private String filename;
    private String result;
    private String[] fieldSet;

    public CustomOrderMapper(String[] fieldSet, long line, String filename, String result) {
        this.line = line;
        this.filename = filename;
        this.result = result;
        this.fieldSet = fieldSet;
    }

    public Order getOrder() {
        Order order = new Order();
        order.setResult(result);
        try {
            long l = Long.parseLong(fieldSet[0]);
            if(l <= 0) new NumberFormatException();
            order.setId(l);
        } catch (NumberFormatException e) {
            order.setResult("ERRORS: The line contains invalid data.");
        }
        try {
            double l = Double.parseDouble(fieldSet[1]);
            if(l <= 0) new NumberFormatException();
            order.setAmount(l);
        } catch (NumberFormatException e) {
            order.setResult("ERRORS: The line contains invalid data.");
        }

        order.setCurrency(fieldSet[2]);
        order.setComment(fieldSet[3]);
        order.setFilename(filename);
        order.setLine(line);

        return order;
    }

    public String getResult() {
        return result;
    }
}
