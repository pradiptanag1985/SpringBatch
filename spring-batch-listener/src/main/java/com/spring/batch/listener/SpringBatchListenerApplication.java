package com.spring.batch.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

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
	public Step deliveryStep() {
		return stepBuilderFactory.get("deliveryStep").tasklet((stepContribution, chunkContext) -> {
			System.out.println("Running the Delivery Step");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Step billingStep() {
		return stepBuilderFactory.get("billingStep").tasklet((stepContribution, chunkContext) -> {
			System.out.println("Running the Billing Step");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Flow deliveryFlow() { //Flow is used to ensure re-usability
		return new FlowBuilder<SimpleFlow>("deliveryFlow").start(arrangeFlowersStep())
				.on("*").to(deliveryStep()).build();
	}

	@Bean
	public Flow billingFlow() { //Flow is used to ensure re-usability
		return new FlowBuilder<SimpleFlow>("billingFlow").start(arrangeFlowersStep())
				.on("*").to(billingStep()).build();
	}

	@Bean
	public Job flowerJob() {
		return jobBuilderFactory.get("flowerJob")
				.start(getFlowerStep())
					.on("trim")
						.to(removeThronesStep())
						.next(deliveryFlow())
				.from(getFlowerStep())
					.on("donttrim")
						.to(deliveryFlow())
				.end()
				.build();
		//Parallel flows using split
		/*return jobBuilderFactory.get("flowerJob")
				.start(getFlowerStep())
				.split(new SimpleAsyncTaskExecutor())
				.add(deliveryFlow(), billingFlow())
				.end()
				.build();*/
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchListenerApplication.class, args);
	}

}
