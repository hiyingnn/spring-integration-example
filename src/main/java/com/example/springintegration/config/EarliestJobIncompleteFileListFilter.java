package com.example.springintegration.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.file.filters.FileListFilter;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class EarliestJobIncompleteFileListFilter implements FileListFilter<File> {
  private final BatchJobMetadataService batchJobMetadataService;

  @Override
  public List<File> filterFiles(File[] files) {

    if (files == null) {
      return List.of();
    }

    List<File> orderedFiles = Arrays.stream(files).sorted(Comparator.comparing(File::getAbsolutePath)).toList();
    for (File file: orderedFiles) {
      if (this.accept(file)) {
        // Only return the first file that is unsuccessful!
        return List.of(file);
      }
    }
    return List.of();
  }

  @Override
  public boolean accept(File file) {
    boolean isSuccessful = batchJobMetadataService.isJobExecutionSuccessful(file.getAbsolutePath());
    return !isSuccessful;
  }

  @Override
  public boolean supportsSingleFileFiltering() {
    return true;
  }
}
