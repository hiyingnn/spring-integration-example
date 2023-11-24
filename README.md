# Requirements
1. Polling file:
   - In order
   - Only those which have (1) failed processing, (2) yet to have processed
2. Batch processing:
   - Process and store changes in database:
     - Update: Update time and detaails
     - Create: Create record
     - Delete: Update flag

# Polling file

## Filters
### `FileSystemPersistentAcceptOnceFileListFilter`:

#### Explanation
- Ensures that on a app restart, we have stored metadata of which files have been polled before.
- Unless the file was last updated since then, it will not be polled
- However, there will be the scenario where:
  - processed files are polled again, regardless of `JobStatus`, as long as it was last updated

#### Implementation Details
- Need to import dependency:
```xml
<dependency>
  <groupId>org.springframework.integration</groupId>
  <artifactId>spring-integration-jdbc</artifactId>
</dependency>
```
- Create `JdbcMetadataStore` Bean which will store and fetch metadata of files (date, last modified date), which will be used in the `FileSystemPersistentAcceptOnceFileListFilter`
```java
@Bean
public JdbcMetadataStore jdbcMetadataStore(DataSource dataSource) {
  return new JdbcMetadataStore(dataSource);
}
```

### `JobSuccessfulFileListFilter`:
- A custom filter condition which can filter out those files which have already been processed.
- However, this does not ensure that the processing is done in order

#### Implementation Details
- A concrete implementation of `AbstractFileListFilter` which will call the `BatchMetadataService`, queries the `JobRepository`
- A file that passes this filter would be one without a BatchStatus.equals(completed):
  - Failed processing
  - Never been processed before

### `Order`:
- We would need to process it in order. Following the javadocs:
```java
public static FileInboundChannelAdapterSpec inboundAdapter(File directory,
 @Nullable
 Comparator<File> receptionOrderComparator){}
```
  - receptionOrderComparator - the Comparator for ordering file objects.
- However, this approach might not always guarantee the files to be processed in the order of their names. The reason lies in the asynchronous nature of message processing in Spring Integration. When the inboundAdapter detects a new file, it creates an IntegrationMessage containing the file and sends it to the downstream components. The processing of these messages is asynchronous, meaning they might not be handled in the same order they were received.
- As such, I have done the sorting in the `JobSuccessfulFileListFilter` as described above
### Advanced
- File transferring halfway?
  - Rename strategy?
  - Use LastModified filters?
---

## Testing
The following is the testing strategy that would like to employ:

### e2e test
1. Positive flow:
   - 2 files
   - assert output of the final outcome
   - Need to ensure that spring batch job metatables are fresh
2. Negative flow:
   - 1 file positive (?), 1 file negative

### Unit testing
#### Spring Integration Flow: File -> Job Gateway:
1. 2 files:
   - Both new (not processed)
2. 2 files:
   - One not new, 1 new (not processed)
3. 2 files:
   - Both failed

#### Spring batch
1. Processing (?)
