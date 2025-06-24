# Java Grep

# Design considerations

# Concurrency

# Future improvements

Think about file access failures
S3 file store implementation. Abstractions gie us necessary machinery
Use Lombok for POJOs
Play with checked Exceptions

## Key points

# Requirements

- Don't use any libraries at all like:
    - Apache Commons
    - Guava
- Two matchers - String and Regexp
- Single thread directory traverser
- Multi-threaded File content search
- Application is rich of types
- Use Futures
- Stdout Consumer
- Search by plain text or regexp abstraction
- Search over S3, local file system abstraction
- Statistics
- Abstcraction for file
- Abscraction for scanner