# Demo Spring Batch

This is a sample application demoing how to launch batch jobs from messages.

## API Reference

- **MessageSource**
  - Base interface for any source of `Messages` that can be polled.
- **FileReadingMessageSource**
  - `MessageSource` that creates messages from a file system directory. To prevent messages for certain files,
    you may supply a `FileListFilter`. By default, when configuring with XML or the DSL, an `AcceptOnceFileListFilter`
    is used. It ensures files are picked up only once from the directory.
- **FileMessageToJobRequest**
  - A custom transformer that creates Job Request from incoming messages
- **JobParametersBuilder**
  - Helper class for creating `JobParameters`. Useful because all `JobParameter` objects are immutable
    and must be instantiated separately to ensure type safety. Once created, it can be used in the
    same was a `StringBuilder` (except that order is irrelevant), by adding various parameter types and
    creating a valid `JobParameters` object once finished.
- **JobLaunchingMessageHandler**
  - Message handler which uses strategies to convert a Message into a job and a set of job parameters
- **JobLaunchRequest**
  - Encapsulation of a `Job` and its `JobParameters` forming a request for a job to be launched.
- **FlatFileItemReader**
  - Restartable `ItemReader` that reads lines from input `setResource(Resource)`. Line is defined by the
    `setRecordSeparatorPolicy(RecordSeparatorPolicy)` and mapped to item using `setLineMapper(LineMapper)`.
    If an exception is thrown during line mapping it is rethrown as `FlatFileParseException` adding information
    about the problematic line and its line number.
- **FileSystemResource**
  - Resource implementation for `java.io.File` and `java.nio.file.Path` handles with a file system target.
    Supports resolution as a `File` and also as a `URL`. Implements the extended `WritableResource` interface.
- **PassThroughLineMapper**
  - Pass through `LineMapper` useful for passing the original `String` back directly rather than a mapped object.
