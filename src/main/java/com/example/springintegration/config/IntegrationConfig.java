package com.example.springintegration.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.handler.LoggingHandler;

import java.io.File;


@Configuration
public class IntegrationConfig {

    @Bean
    public FileMessageToJobRequest fileMessageToJobRequest(Job readLogJob) {
        return new FileMessageToJobRequest("input.file.name", readLogJob);
    }

    @Bean
    public JobLaunchingGateway jobLaunchingGateway(JobRepository jobRepository) {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SyncTaskExecutor());

        return new JobLaunchingGateway(jobLauncher);
    }

    @Bean
    public IntegrationFlow integrationFlow(JobLaunchingGateway jobLaunchingGateway, FileMessageToJobRequest fileMessageToJobRequest) {
        return IntegrationFlow.from(Files.inboundAdapter(new File("src/filedump"))
                                        .filter(new SimplePatternFileListFilter("*.csv")),
                        c -> c.poller(Pollers.fixedRate(1000).maxMessagesPerPoll(1)))
                .transform(fileMessageToJobRequest)
                .handle(jobLaunchingGateway)
                .log(LoggingHandler.Level.WARN, "headers.id + ': ' + payload")
                .get();
    }
}
