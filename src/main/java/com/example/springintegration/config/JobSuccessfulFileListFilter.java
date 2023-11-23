package com.example.springintegration.config;

import org.springframework.integration.file.filters.AbstractPersistentAcceptOnceFileListFilter;
import org.springframework.integration.metadata.ConcurrentMetadataStore;

public class JobSuccessfulFileListFilter extends AbstractPersistentAcceptOnceFileListFilter {
  public JobSuccessfulFileListFilter(ConcurrentMetadataStore store, String prefix) {
    super(store, prefix);
  }


  @Override
  public boolean accept(Object file) {
    return super.accept(file);
  }

  @Override
  protected long modified(Object file) {
    return 0;
  }

  @Override
  protected String fileName(Object file) {
    return null;
  }
}
