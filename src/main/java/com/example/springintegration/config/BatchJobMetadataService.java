package com.example.springintegration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchJobMetadataService {
  private final JobRepository jobRepository;

  public boolean isJobExecutionSuccessful(String fileName){
    JobExecution jobExecution = jobRepository.getLastJobExecution("readLogOrderJob", new JobParametersBuilder().addString("input.file.name", fileName).toJobParameters());
    return jobExecution != null && jobExecution.getStatus().equals(BatchStatus.COMPLETED);
  }

}
