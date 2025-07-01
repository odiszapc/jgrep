# Multi-threaded Grep Tool (Java)

This project implements a recursive, multi-threaded Grep-like utility in Java, inspired by the Linux `grep` command.

It searches for lines matching a given pattern in all files under a specified directory.

## Features

- Recursively searches all files in a directory
- Supports both plain string and regular expression matching
- Uses a thread pool to parallelize file processing
- Print results in `filename:line_number:line` format
- Tracks statistics such as matched lines, processed bytes, and files

## Usage

Run the application using the `Main` class:

```bash
java -cp target/grep-java.jar io.odiszapc.jgrep.Main /path/to/dir "searchPattern"
````

Or programmatically:

```java
JGrep.plainSearch(store, rootDir, pattern, numThreads);
```

## Usage examples:

```java
// Contains string search
JGrep.plainSearch(FileSystemStore.defaultFS(), "/", "foo", 4);

// Regex search
JGrep.regexSearch(FileSystemStore.defaultFS(), "/", "/foo/", 4);

// Search ignoring letter case
JGrep.ignireCaseSearch(FileSystemStore.defaultFS(), "/", "/FOO/", 4);
```

You can customize `ObjectStore implementation as well as provide your own versions of
`Matcher`, `OutputPrinter` and `StatisticsPrinter`, lambda-driven example:

```java
JGrep.create(path -> {
    // Use local file system by default
    return FileSystemDescriptor.of(Path.of(path));
}, "/foo", 4, line -> {
    // Match every line
    return true;
}, lineMatch -> {
    // Print matched line to stdout
    System.out.println(lineMatch.toString());
}, statistics -> {
    // Print numbers
    statistics.filesProcessed();
    statistics.linesProcessed();
    statistics.bytesProcessed();
    statistics.linesMatched();
})
  .startAsync()
  .waitForFinish();
```

## Design Considerations

* Directory traversal is performed using a **single thread**. This could be optimized in the future,
  but for local HDDs it's sufficient due to the physical limitations of mechanical spindles.
* File content is searched in a **multi-threaded** manner using a thread pool.
* A strong abstraction layer exists over the file system:
* Files and directories are treated as `Objects`
* The file system is modeled via the `ObjectStore` interface
* Directory iteration is abstracted, allowing reuse across different storage implementations

## Multithreading

* `ExecutorService` is used to parallelize file reading and line matching
* Each discovered file is submitted as a task to the thread pool
* File content is processed independently using the `TextSearch` class
* Statistics are updated using `AtomicInteger` and synchronized methods

The thread pool ensures efficient CPU utilization when processing multiple files.

The process of traversing over the directory hierarchy was considered to be implemented in a single thread.
It can be improved if we want to travers over distributed remote file tree like S3 buckets

## Testability

Core components are loosely coupled and easily testable in isolation.

Thanks to the abstraction layers, each component can be independently tested:

* File system — `ObjectStore`
* File path — `ObjectDescriptor`
* File and input stream — `ObjectData`
* Directory iterator — `ObjectsIterable`

Every single layer of this architecture can be tested independently.

Basic unit tests are provided for:

* Matchers (`SimpleMatcher`, `RegexMatcher`)
* `Grep` tool (for line matching and output handling)

To run tests:

```bash
mvn test
```

## What could be improved

* Handle file access failures more gracefully (e.g., permission errors, encoding issues)
* Implement an AWS S3-backed `ObjectStore` — the current abstractions support this
* Use Lombok to reduce boilerplate in POJOs
* Create deep tests using virtual directory trees to detect issues with symlinks, deeply nested directories, etc.
* Improve exception handling and reconsider the use of checked exceptions
* Allow parallel parsing of object parts — useful for a small number of very large files; this would require
  an `ObjectPartition` abstraction
* Design a **distributed grep** version that can run across multiple machines with coordination via Consul or Zookeeper