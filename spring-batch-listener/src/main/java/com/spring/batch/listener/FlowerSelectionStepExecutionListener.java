package com.spring.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class FlowerSelectionStepExecutionListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Running before step");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Running after step");
        String flowerType = stepExecution.getJobExecution().getJobParameters().getString("flowerType");
        return ("rose".equalsIgnoreCase(flowerType)) ? new ExitStatus("trim") : new ExitStatus("donttrim");
    }
}
