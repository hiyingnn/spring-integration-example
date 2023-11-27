package com.example.springintegration.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(properties = { "integration.file-destination=src/test/resources/csv-files" })
public class SpringBatchIntegrationTest {
  @Autowired
  JobLauncherTestUtils jobLauncherTestUtils;

//  public StepExecution getStepExecution() {
//    StepExecution execution = MetaDataInstanceFactory.createStepExecution();
//    execution.getExecutionContext().putString("input.file.name", "classpath:test/csv-files/test-9.csv" );
//    return execution;
//  }

  @Test
  public void test() throws Exception {
    // The reader is initialized and bound to the input data
    jobLauncherTestUtils.launchJob(new JobParametersBuilder()
        .addString("input.file.name", "/Users/renyang/Desktop/CSIT/poc/spring-integration/spring-integration-example/src/test/resources/csv-files/test-9.csv" )
      .toJobParameters());
  }
}
