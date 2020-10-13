# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/maven-plugin/reference/html/#build-image)
* [Spring Batch](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#howto-batch-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)

@EnableBatchProcessing - Several beans will be registered in Spring IOC Container - Job Repository, Job Launcher,
Job Registry, Transaction Manager, Job Builder Factory, Step Builder Factory

JobInstance - Job Name + Job Parameters
JobExecution - Executes Job Instance

JobInstance may remain same in case of failures, but JobExecution will be different each time of execution

Each execution of Step is going to create a StepExecution which will be connected to JobExecution

Step - Tasklet, Chunk Based Step(Processes data from data store)

Job Repository - Stores metadata in Spring Batch metadata tables

Parameters are passed like - "item=Shoe" "run.date(date)=2020/10/13"

Restartability - If a step is already completed then that step will not be executed in case the job fails in next subsequent steps.