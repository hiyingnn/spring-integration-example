package com.example.springintegration.model;

import lombok.Builder;

import java.util.Date;

@Builder
public record Order(String referenceId, String name, Date firstUpdated, Date lastUpdated) {
}
