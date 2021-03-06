package com.example.orders_parser.readers;

import com.example.orders_parser.OrdersParserApplication;
import com.example.orders_parser.entities.Order;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.batch.item.ItemReader;

import lombok.Data;

import java.io.BufferedReader;
import java.io.File;


@Data
public class CustomJsonFileItemReader implements ItemReader<Order> {

    private static String currentLine;
    private static long currentNumberOfCurrentLine;
    private static String currentStatusOfCurrentLine = "OK";

    private File file;
    private BufferedReader bufferedReader;

    @Override
    public synchronized Order read() throws Exception {

        if (bufferedReader != null) {
            currentLine = bufferedReader.readLine();
            currentNumberOfCurrentLine++;
        }

        if (currentLine != null) {

            Order order = getOrderFromLine(currentLine);
            currentLine = null;

            order.setLine(currentNumberOfCurrentLine);
            order.setFilename(file.getName());
            order.setResult(currentStatusOfCurrentLine);

            currentStatusOfCurrentLine = "OK";

            return order;
        }

        setDefaultValuesForFields();

        OrdersParserApplication.closeResourceForJSON();

        return null;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    private Order getOrderFromLine(String line) {

        ObjectMapper objectMapper = new ObjectMapper();
        Order orderJSON;
        try {
            orderJSON = objectMapper.readValue(line, Order.class);
            return orderJSON;
        } catch (JsonParseException e) {
            currentStatusOfCurrentLine = "ERRORS: The line contains invalid data. " + e.getMessage();
            orderJSON = new Order();
            return orderJSON;
        } catch (JsonProcessingException e) {
            currentStatusOfCurrentLine = "ERRORS: The line contains invalid data. " + e.getMessage();
            orderJSON = new Order();
            return orderJSON;
        }
    }

    private void setDefaultValuesForFields() {
        currentLine = null;
        currentNumberOfCurrentLine = 0;
        currentStatusOfCurrentLine = "OK";
        file = null;
        bufferedReader = null;
    }
}
