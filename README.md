# Multi-threaded Grep Tool (Java)

This project implements a recursive, multithreaded Grep-like utility in Java, inspired by the Linux `grep` command.

It searches for lines matching a given pattern in all files under a specified directory.

## Features

- Recursively searches all files in a directory
- Supports plain string and regex matching
- Uses a thread pool to parallelize file processing
- Prints results in `filename:line_number:line` format
- Tracks statistics like matched lines, processed bytes/files

## Usage

Run the application using the `Main` class:

```bash
java -cp target/grep-java.jar io.odiszapc.jgrep.Main /path/to/dir "searchPattern"
````

Or programmatically:

```java
Grep.plainSearch(store, rootDir, pattern, numThreads);
Grep.regexSearch(store, rootDir, regexPattern, numThreads);
```

## Multithreading Design

* Uses `ExecutorService` to parallelize file reading and matching.
* Each discovered file is submitted as a task to the thread pool.
* File content is processed independently using the `TextSearch` class.
* Statistics are updated using `AtomicInteger` and synchronized methods.

## Clean Code & Testability

* Logic is modular and built on interfaces (`Matcher`, `Output`, etc.)
* Supports easy extension and testing
* Avoids using full frameworks (e.g., Spring)

## Design Considerations

We traverse directories using a **single thread**. This could be improved in the future, but for local HDDs it's acceptable
  due to the nature of mechanical spindles.

File content is searched in a **multi-threaded** way using a thread pool.

**Scalability**: Thread pool ensures efficient CPU utilization across files.

There is a strong **abstraction layer over the file system**:
* Files and directories are treated as `Objects`
* The filesystem is modeled as an `ObjectStore`
* Directory iteration is abstracted, allowing the same logic to be reused across different storage implementations

**Testability**: Core components are loosely coupled and easily testable in isolation.

## Tests

Basic unit tests are provided for:

* Matchers (`SimpleMatcher`, `RegexMatcher`)
* TextSearch (for line matching and output behavior)

To run tests:

```bash
mvn test
```

## What to Improve

* Handle file access failures more gracefully (e.g., permissions, encoding issues)
* Implement an AWS S3-backed `ObjectStore` — the abstractions already support this
* Use Lombok to reduce boilerplate in POJOs
* Improve exception handling (rethink use of checked exceptions)
* Parse parts of each object in parallel, can be reasonable for small number of very big objects, `ObjectPartititon` abstraction is needed then.
* Design a **distributed grep** that runs across multiple machines with coordination via Consul or Zookeeper
