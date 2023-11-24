package com.example.springintegration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.file.filters.FileListFilter;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class EarliestJobIncompleteFileListFilter implements FileListFilter<File> {
  private final BatchJobMetadataService batchJobMetadataService;

  @Override
  public List<File> filterFiles(File[] files) {

    if (files == null) {
      return List.of();
    }

    List<File> orderedFiles = Arrays.stream(files).sorted(Comparator.comparing(File::getName)).toList();
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
    boolean isSuccessful = batchJobMetadataService.isJobExecutionSuccessful(file.getName());
    return !isSuccessful;
  }

  @Override
  public boolean supportsSingleFileFiltering() {
    return true;
  }
}
