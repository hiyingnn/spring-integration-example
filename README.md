# Autoinferring 
Automapping FieldSets to Domain Objects
For many, having to write a specific FieldSetMapper is equally as cumbersome as writing a specific RowMapper for a JdbcTemplate. Spring Batch makes this easier by providing a FieldSetMapper that automatically maps fields by matching a field name with a setter on the object using the JavaBean specification.

Again using the football example, the BeanWrapperFieldSetMapper configuration looks like the following snippet in Java:

Java Configuration
``` java
@Bean
public FieldSetMapper fieldSetMapper() {
BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper();

	fieldSetMapper.setPrototypeBeanName("player");

	return fieldSetMapper;
}

```

```java
@Bean
@Scope("prototype")
public Player player() {
    return new Player();
}
```


```txt
2023-11-22T18:35:55.271+08:00 ERROR 11572 --- [   scheduling-1] o.s.integration.handler.LoggingHandler   : org.springframework.messaging.MessageHandlingException: error occurred in message handler [bean 'integrationFlow.bridge#0' for component 'integrationFlow.org.springframework.integration.config.ConsumerEndpointFactoryBean#2'; defined in: 'class path resource [com/example/springintegration/config/IntegrationConfig.class]'; from source: 'bean method integrationFlow'], failedMessage=GenericMessage [payload=JobExecution: id=82, version=2, startTime=2023-11-22T18:35:55.262749800, endTime=2023-11-22T18:35:55.271750300, lastUpdated=2023-11-22T18:35:55.271750300, status=FAILED, exitStatus=exitCode=FAILED;exitDescription=org.springframework.batch.item.ReaderNotOpenException: Reader must be open before it can be read.
```
https://stackoverflow.com/questions/23847661/spring-batch-org-springframework-batch-item-readernotopenexception-reader-must