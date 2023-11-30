package com.example.springintegration.e2e;

import com.example.springintegration.mapper.OrderRecordMapper;
import com.example.springintegration.model.OrderRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.Resource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Testcontainers
@SpringBatchTest
@SpringBootTest
@Slf4j
@Sql(
  scripts = "delete-test-data.sql",
  executionPhase = AFTER_TEST_METHOD
)
class SpringBatchIntegrationTest {

  @Autowired
  JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired
  JobRepositoryTestUtils jobRepositoryTestUtils;

  @Autowired
  JobRepository jobRepository;



  @MockBean
  IntegrationFlow integrationFlow;

  @Autowired
  OrderRecordMapper orderRecordMapper;

  @Container
  @ServiceConnection
  public static OracleContainer oracle = new OracleContainer("gvenzl/oracle-xe:latest")
    .withInitScript("init-sql/schema-oracle.sql");
  @Test
  @Sql(scripts="classpath:/testcases/positive/testcase-1.sql")
  void myTest( @Value("classpath:/testcases/positive/testcase-1.csv") Resource resource) throws Exception {

    String fileName = resource.getFile().getAbsolutePath();
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder().addString("input.file.name", fileName).toJobParameters());

    List<OrderRecord> orderRecordsFromDb = orderRecordMapper.selectOrderRecords();
    OrderRecord orderExisting = OrderRecord.builder()
      .referenceId("1")
      .name("Chocolate")
      .firstUpdated(Date.valueOf("2021-1-1"))
      .lastUpdated(Date.valueOf("2021-2-1"))
      .isDeleted(false)
      .build();

    OrderRecord orderAddedApple = OrderRecord.builder()
        .referenceId("2")
        .name("Apple")
        .firstUpdated(Date.valueOf("2022-1-1"))
        .lastUpdated(Date.valueOf("2022-2-1"))
        .isDeleted(false)
       .build();

    OrderRecord orderAddedBanana = OrderRecord.builder()
      .referenceId("3")
      .name("Banana")
      .firstUpdated(Date.valueOf("2023-01-01"))
      .lastUpdated(Date.valueOf("2023-02-01"))
      .isDeleted(false)
      .build();

    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    assertThat(orderRecordsFromDb).size().isEqualTo(3);
//    assertThat(orderRecordsFromDb).containsExactlyInAnyOrder(orderAddedApple, orderAddedBanana, orderExisting);

    // ...
  }

}
