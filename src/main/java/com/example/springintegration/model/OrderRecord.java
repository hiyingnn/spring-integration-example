package com.example.springintegration.model;

import lombok.Builder;

import java.util.Date;

@Builder
public record OrderRecord(String referenceId,
                          String name,
                          Date firstUpdated,
                          Date lastUpdated,
                          boolean isDeleted) {

  @Override

  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null) {
      return false;
    }
    if (!(other instanceof OrderRecord orderRecord)) {
      return false;
    }
    if (orderRecord.referenceId.equals(this.referenceId)
      && orderRecord.name.equals(this.name)
      && orderRecord.firstUpdated.equals(this.firstUpdated)
      && orderRecord.lastUpdated.equals(this.lastUpdated)
    ) {
      return true;
    }
    return false;
  }
}
