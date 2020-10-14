package com.spring.batch.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
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
public class SpringBatchListenerApplication {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public StepExecutionListener flowerSelectionStepExecutionListener() {
		return new FlowerSelectionStepExecutionListener();
	}

	@Bean
	public Step getFlowerStep() {
		return stepBuilderFactory.get("getFlowStep").tasklet((stepContribution, chunkContext) -> {
			System.out.println("Running the Get Flow Step");
			return RepeatStatus.FINISHED;
		}).listener(flowerSelectionStepExecutionListener()).build();
	}

	@Bean
	public Step removeThronesStep() {
		return stepBuilderFactory.get("removeThronesStep").tasklet((stepContribution, chunkContext) -> {
			System.out.println("Running the Remove Thrones Step");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Step arrangeFlowersStep() {
		return stepBuilderFactory.get("arrangeFlowersStep").tasklet((stepContribution, chunkContext) -> {
			System.out.println("Running the Arrange Flowers Step");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Job flowerJob() {
		return jobBuilderFactory.get("flowerJob")
				.start(getFlowerStep())
					.on("trim")
						.to(removeThronesStep()).next(arrangeFlowersStep())
				.from(getFlowerStep())
					.on("donttrim")
						.to(arrangeFlowersStep())
				.end()
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchListenerApplication.class, args);
	}

}
