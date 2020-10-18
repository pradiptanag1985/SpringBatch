package com.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class SpringBatchFlatFileReaderApplication {

    public static String[] tokens = new String[]{"Serial Number", "Company Name", "Employee Markme", "Description", "Leave"};

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Scheduled(cron = "0/30 * * * * *")
    public void runJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("runTime", new Date());
        this.jobLauncher.run(chunkBasedJob(), jobParametersBuilder.toJobParameters());
    }

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
    public ItemProcessor<DataHolder, DataHolder> itemProcessor() {
        BeanValidatingItemProcessor<DataHolder> itemProcessor = new BeanValidatingItemProcessor<>();
        itemProcessor.setFilter(true);
        return itemProcessor;
    }

    @Bean
    public ItemProcessor<DataHolder, DataHolder> customItemProcessor() {
        return new CustomItemProcessor();
    }


    @Bean
    public Step chunkBasedStep() {
        return this.stepBuilderFactory.get("flatFileReaderStep")
                .<DataHolder, DataHolder>chunk(20)
                .reader(itemReader())
                .processor(itemProcessor())
                .processor(customItemProcessor())
                .faultTolerant()
                .skip(IllegalStateException.class)
                .skipLimit(50)
                .listener(new CustomSkipListener())
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
