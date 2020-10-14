package com.learning.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchApplication {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step packageItemStep() {
        return this.stepBuilderFactory.get("packageItemStep")
                .tasklet((stepContribution, chunkContext) -> {
                    //Getting the Job Parameters
                    String itemParameter = chunkContext.getStepContext().getJobParameters().get("item").toString();
                    String dateParameter = chunkContext.getStepContext().getJobParameters().get("run.date").toString();
                    System.out.println(String.format("%s has been packaged on %s", itemParameter, dateParameter));
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step deliverToCustomerStep() {
        boolean isGotLost = false;
        return this.stepBuilderFactory.get("deliverToCustomerStep")
                .tasklet((stepContribution, chunkContext) -> {
                    if (isGotLost)
                        throw new RuntimeException("Got lost while trying to deliver item");

                    System.out.println("Delivering the package to the customer");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step customerReceiveStep() {
        return this.stepBuilderFactory.get("customerReceiveStep")
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("Customer received the package");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step customerNotReceiveStep() {
        return this.stepBuilderFactory.get("customerNotReceiveStep")
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("Customer didn't receive the package");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Job deliverPackage() {
        return this.jobBuilderFactory.get("deliverPackageJob")
                .start(packageItemStep())
                .next(deliverToCustomerStep())
                .on("FAILED").to(customerNotReceiveStep())
                .from(deliverToCustomerStep())
                .on("*").to(customerReceiveStep())
                .end()
                .build();
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

}
