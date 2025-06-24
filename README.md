# Multi-threaded Grep Tool (Java)

This project implements a recursive, multi-threaded Grep-like utility in Java, inspired by the Linux `grep` command.

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

## Design considerations

We traverse directories using a **single thread**. This could be improved in the future, but for local HDDs it's acceptable
  due to the nature of mechanical spindles.

File content is searched in a **multi-threaded** way using a thread pool.

There is a strong abstraction layer over the file system:
* Files and directories are treated as `Objects`
* The filesystem is modeled as an `ObjectStore`
* Directory iteration is abstracted, allowing the same logic to be reused across different storage implementations

In terms of multithreading the following points were considered:

* Uses `ExecutorService` to parallelize file reading and matching.
* Each discovered file is submitted as a task to the thread pool.
* File content is processed independently using the `TextSearch` class.
* Statistics are updated using `AtomicInteger` and synchronized methods.

Thread pool ensures efficient CPU utilization across files.

## Testability

Core components are loosely coupled and easily testable in isolation.

As we have an abstraction layer over different kind of components

* Filesystem — `ObjectStore`
* File path — `ObjectDescriptor`
* File and InputStream — `ObjectData`
* Directory iterator — `ObjectsIterable`

Every single layer of this architecture can be tested independently.

Basic unit tests are provided for:

* Matchers (`SimpleMatcher`, `RegexMatcher`)
* TextSearch (for line matching and output behavior)

To run tests:

```bash
mvn test
```

## What to improve

* Handle file access failures more gracefully (e.g., permissions, encoding issues)
* Implement an AWS S3-backed `ObjectStore` — the abstractions already support this
* Use Lombok to reduce boilerplate in POJOs
* Consider deep test agains virtual directory tree to catch symlinks, deep level of directories, etc
* Improve exception handling (rethink use of checked exceptions)
* Parse parts of each object in parallel, can be reasonable for small number of very big objects, `ObjectPartititon` abstraction is needed then.
* Design a **distributed version of grep** that runs across multiple machines with coordination via Consul or Zookeeper
