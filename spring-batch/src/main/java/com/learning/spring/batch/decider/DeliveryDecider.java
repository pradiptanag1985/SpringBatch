package com.learning.spring.batch.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.time.LocalDateTime;

/**
 * Decider to decide Custom Exit Status
 */
public class DeliveryDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String result = (LocalDateTime.now().getHour() < 12) ? "PRESENT" : "ABSENT";
        return new FlowExecutionStatus(result);
    }
}
