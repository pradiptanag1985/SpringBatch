package com.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchFlatFileReaderApplication {

    public static String[] tokens = new String[] {"Serial Number", "Company Name", "Employee Markme", "Description", "Leave"};

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<DataHolder> itemReader() {
        FlatFileItemReader<DataHolder> itemReader = new FlatFileItemReader<>();
        itemReader.setLinesToSkip(1); //Skip the header
        itemReader.setResource(new FileSystemResource("src/main/resources/sample-data.csv"));

        DefaultLineMapper<DataHolder> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(tokens);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new DataHolderFieldSetMapper());
        itemReader.setLineMapper(lineMapper);
        return itemReader;
    }

    @Bean
    public Step chunkBasedStep() {
        return this.stepBuilderFactory.get("flatFileReaderStep")
                .<DataHolder, DataHolder>chunk(20)
                .reader(itemReader())
                .writer(items -> {
                    System.out.println(String.format("Received a list of size %s", items.size()));
                    items.forEach(System.out::println);
                }).build();
    }


    @Bean
    public Job chunkBasedJob() {
        return this.jobBuilderFactory.get("flatFileReaderJob")
                .start(chunkBasedStep())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchFlatFileReaderApplication.class, args);
    }

}
