package com.example.orders_parser.readers;

import com.example.orders_parser.OrdersParserApplication;
import com.example.orders_parser.entities.Order;
import com.example.orders_parser.mappers.CustomOrderMapper;

import lombok.Data;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;


@Data
public class CustomCsvFileItemReader implements ItemReader<Order> {

    private static String currentLine;
    private static long currentNumberOfCurrentLine;
    private static String currentStatusOfCurrentLine = "OK";

    private File file;
    private BufferedReader bufferedReader;

    @Autowired
    CustomOrderMapper customOrderMapper;

    @Override
    public synchronized Order read() throws Exception {

        if (bufferedReader != null) {
            currentLine = bufferedReader.readLine();
            currentNumberOfCurrentLine++;
        }

        if (currentLine != null) {

            String[] fieldSet = getFieldSetFromLine(currentLine);
            currentLine = null;

            customOrderMapper.setFieldSet(fieldSet);
            customOrderMapper.setLine(currentNumberOfCurrentLine);
            customOrderMapper.setFilename(file.getName());
            customOrderMapper.setResult(currentStatusOfCurrentLine);

            return customOrderMapper.getOrder();
        }

        setDefaultValuesForFields();

        OrdersParserApplication.closeResourceForCSV();

        return null;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    private void setDefaultValuesForFields() {
        currentLine = null;
        currentNumberOfCurrentLine = 0;
        currentStatusOfCurrentLine = "OK";
        file = null;
        bufferedReader = null;
    }

    private String[] getFieldSetFromLine(String line) {
        return line.split(",");
    }

}
