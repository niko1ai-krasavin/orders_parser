package com.example.orders_parser.readers;

import com.example.orders_parser.entities.Order;
import com.example.orders_parser.mappers.CustomOrderMapper;

import lombok.Data;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.*;


@Data
public class CustomCsvFileItemReader implements ItemReader<Order> {

    private static String currentLine;
    private static long currentNumberOfCurrentLine;
    private static String currentStatusOfCurrentLine = "OK";

    private File file;
    private BufferedReader bufferedReader;

    @Override
    public Order read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if(bufferedReader != null) {
            currentLine = bufferedReader.readLine();
            currentNumberOfCurrentLine++;
        }

        if (currentLine != null) {

            String[] fieldSet = getFieldSetFromLine(currentLine);
            currentLine = null;

            CustomOrderMapper customOrderMapper =
                    new CustomOrderMapper(fieldSet, currentNumberOfCurrentLine, file.getName(), currentStatusOfCurrentLine);

            return customOrderMapper.getOrder();
        }
        return null;
    }

    private String[] getFieldSetFromLine(String line) {
        return line.split(",");
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }
}
