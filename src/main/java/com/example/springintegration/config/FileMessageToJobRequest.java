package com.example.springintegration.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import java.io.File;


public class FileMessageToJobRequest {
    private final Job job;
    private final String fileParameterName;

    public FileMessageToJobRequest( String fileParameterName, Job job) {
        this.job = job;
        this.fileParameterName = fileParameterName;
    }

    /**
     * Message transformers play a very important role in enabling the loose-coupling of message producers and message consumers.
     * Rather than requiring every message-producing component to know what type is expected by the next consumer,
     * you can add transformers between those components
     * @param message
     * @return
     */

    @Transformer
    public JobLaunchRequest toRequest(Message<File> message) {
        JobParametersBuilder jobParametersBuilder =
            new JobParametersBuilder();

        jobParametersBuilder.addString(fileParameterName,
            message.getPayload().getAbsolutePath());

        return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
    }
}