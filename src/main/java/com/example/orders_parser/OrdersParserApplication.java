package com.example.orders_parser;

import com.example.orders_parser.readers.CustomCsvFileItemReader;
import com.example.orders_parser.readers.CustomJsonFileItemReader;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.*;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class OrdersParserApplication implements CommandLineRunner {

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

    public static void main(String[] args) {
        SpringApplication.run(OrdersParserApplication.class, args);
        //logging.pattern.console=
    }

    @Override
    public void run(String... args) throws Exception {

        File fileCSV = new File("src/main/resources/", args[0]);
        File fileJson = new File("src/main/resources/", args[1]);
        /*   //for deploy
        File fileCSV = new File("./classes/", args[0]);
        File fileJson = new File("./classes/", args[1]);
         */

        FileReader fileReaderCSV = new FileReader(fileCSV);
        BufferedReader brCSV = new BufferedReader(fileReaderCSV);
        customCsvFileItemReader.setFile(fileCSV);
        customCsvFileItemReader.setBufferedReader(brCSV);

        FileReader fileReaderJson = new FileReader(fileJson);
        BufferedReader brJSON = new BufferedReader(fileReaderJson);
        customJsonFileItemReader.setFile(fileJson);
        customJsonFileItemReader.setBufferedReader(brJSON);

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("file.csv", fileCSV.toString());

        try {
            jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

}