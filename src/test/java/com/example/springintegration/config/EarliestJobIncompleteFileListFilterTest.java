package com.example.springintegration.config;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EarliestJobIncompleteFileListFilterTest {
  @Mock(strictness = Mock.Strictness.LENIENT)
  BatchJobMetadataService batchJobMetadataService;

  @InjectMocks
  EarliestJobIncompleteFileListFilter earliestJobIncompleteFileListFilter;

  @Test
  public void givenAllJobsSuccessfulStatus_whenFileFilter_returnEmpty() {
    File file1 = new File("file1");
    File file2 = new File("file2");

    File[] files = new File[2];
    files[0] = file1;
    files[1] = file2;

    // Given file1:unsuccessful, file2:unsuccessful
    // Then: return empty
    when(batchJobMetadataService.isJobExecutionSuccessful(file1.getName()))
      .thenReturn(true);
    when(batchJobMetadataService.isJobExecutionSuccessful(file2.getName()))
      .thenReturn(true);

    List<File> filteredFiles = earliestJobIncompleteFileListFilter.filterFiles(files);
    // Then: return empty
    assertThat(filteredFiles).isEmpty();
  }

  @Test
  public void givenAllJobsUnsuccessfulStatus_whenFileFilter_returnFirstUnsuccesful() {
    File file1 = new File("file1");
    File file2 = new File("file2");

    File[] files = new File[2];
    files[0] = file1;
    files[1] = file2;

    // Given file1:unsuccessful, file2:unsuccessful
    // Then: return empty
    when(batchJobMetadataService.isJobExecutionSuccessful(file1.getName()))
      .thenReturn(false);
    when(batchJobMetadataService.isJobExecutionSuccessful(file2.getName()))
      .thenReturn(false);

    List<File> filteredFiles = earliestJobIncompleteFileListFilter.filterFiles(files);
    // Then: return first file
    assertThat(filteredFiles).containsExactly(file1);
  }

  @Test
  public void givenSecondJobsUnsuccessfulStatus_whenFileFilter_returnFirstUnsuccesful() {
    File file1 = new File("file1");
    File file2 = new File("file2");

    File[] files = new File[2];
    files[0] = file1;
    files[1] = file2;

    // Given file1:successful, file2:unsuccessful
    // Then: return empty
    when(batchJobMetadataService.isJobExecutionSuccessful(file1.getName()))
      .thenReturn(true);
    when(batchJobMetadataService.isJobExecutionSuccessful(file2.getName()))
      .thenReturn(false);

    List<File> filteredFiles = earliestJobIncompleteFileListFilter.filterFiles(files);
    // Then: return second file
    assertThat(filteredFiles).containsExactly(file2);
  }

}
