package com.example.orders_parser;

import com.example.orders_parser.entities.Order;
import com.example.orders_parser.readers.CustomCsvFileItemReader;
import com.example.orders_parser.readers.CustomJsonFileItemReader;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class OrdersParserApplication implements CommandLineRunner {

    public static Map<Long, Order> mapOrders = new HashMap<>();

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("job")
    Job job;

    @Autowired
    @Qualifier("customCsvFileItemReader")
    CustomCsvFileItemReader customCsvFileItemReader;

    @Autowired
    @Qualifier("customJsonFileItemReader")
    CustomJsonFileItemReader customJsonFileItemReader;

    @Value("${path_to_directory}")
    private String PATH_TO_DIRECTORY;   //for release: "./classes/"
                                        //for develop: "src/main/resources/"
    private static File fileCSV;
    private static File fileJson;

    private static FileReader fileReaderForCSV;
    private static FileReader fileReaderForJSON;

    private static BufferedReader bufferedReaderForCSV;
    private static BufferedReader bufferedReaderForJSON;

    public static void main(String[] args) {
        SpringApplication.run(OrdersParserApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        if (isCountOfFilesValid(args)) {
            if (isFormatOfFilesValid(args)) {
                setFiles(args);
            }
        }

        if (fileCSV.isFile() && fileCSV.canRead()) {
            fileReaderForCSV = new FileReader(fileCSV);
            bufferedReaderForCSV = new BufferedReader(fileReaderForCSV);

            customCsvFileItemReader.setFile(fileCSV);
            customCsvFileItemReader.setBufferedReader(bufferedReaderForCSV);
        } else {
            System.out.println("The file '" + fileCSV.getName() + "' does not exist or is not readable.\n" +
                    "Result: The program was not executed.");
            System.exit(0);
        }

        if (fileJson.isFile() && fileJson.canRead()) {
            fileReaderForJSON = new FileReader(fileJson);
            bufferedReaderForJSON = new BufferedReader(fileReaderForJSON);

            customJsonFileItemReader.setFile(fileJson);
            customJsonFileItemReader.setBufferedReader(bufferedReaderForJSON);
        } else {
            System.out.println("The file '" + fileJson.getName() + "' does not exist or is not readable.\n" +
                    "Result: The program was not executed.");
            System.exit(0);
        }

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("file.csv", fileCSV.toString());

        try {
            jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException
                | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }

        printOrdersFromMap();
    }

    public static boolean closeResourceForCSV() {
        try {
            bufferedReaderForCSV.close();
            fileReaderForCSV.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean closeResourceForJSON() {
        try {
            bufferedReaderForJSON.close();
            fileReaderForJSON.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean isCountOfFilesValid(String[] args) {
        if (args.length > 2) {
            System.out.println("You specified more than 2 files to parsing.\n" +
                    "Specify exactly 2 files. One file in '.csv' format, another file in '.json' format.\n" +
                    "Result: The program was not executed.");
            System.exit(0);
        } else if (args.length == 0) {
            System.out.println("You didn't specify files to parse.\n" +
                    "Specify exactly 2 files. One file in '.csv' format, another file in '.json' format.\n" +
                    "Result: The program was not executed.");
            System.exit(0);
        }
        return true;
    }

    private boolean isFormatOfFilesValid(String[] args) {
        int flag = 0;
        for (String arg : args) {
            if (arg.matches(".+\\.csv$")) {
                flag++;
            }
            if (arg.matches(".+\\.json$")) {
                flag++;
            }
        }
        if (flag != args.length) {
            System.out.println("You specified files with the wrong format.\n" +
                    "Specify exactly 2 files. One file in '.csv' format, another file in '.json' format.\n" +
                    "Result: The program was not executed.");
            System.exit(0);
        }
        return true;
    }

    private void setFiles(String[] args) {
        for (byte i = 0; i < args.length; i++) {
            if (args[i].matches(".+\\.csv$")) {
                fileCSV = new File(PATH_TO_DIRECTORY, args[i]);
            } else if (args[i].matches(".+\\.json$")) {
                fileJson = new File(PATH_TO_DIRECTORY, args[i]);
            }
        }
    }

    private void printOrdersFromMap() {
        for(Order order : mapOrders.values()) {
            System.out.println(order);
        }
    }
}