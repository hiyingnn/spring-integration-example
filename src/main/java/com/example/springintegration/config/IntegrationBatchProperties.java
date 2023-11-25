package com.example.springintegration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integration")
public record IntegrationBatchProperties(
  String fileDestination,
  String cron
) {

}
