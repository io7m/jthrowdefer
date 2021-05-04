jdeferthrow
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.jdeferthrow/com.io7m.jdeferthrow.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.jdeferthrow%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.io7m.jdeferthrow/com.io7m.jdeferthrow.svg?style=flat-square)](https://oss.sonatype.org/content/repositories/snapshots/com/io7m/jdeferthrow/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m/jdeferthrow.svg?style=flat-square)](https://codecov.io/gh/io7m/jdeferthrow)

![jdeferthrow](./src/site/resources/jdeferthrow.jpg?raw=true)

| JVM             | Platform | Status |
|-----------------|----------|--------|
| OpenJDK LTS     | Linux    | [![Build (OpenJDK LTS, Linux)](https://img.shields.io/github/workflow/status/io7m/jdeferthrow/main-openjdk_lts-linux)](https://github.com/io7m/jdeferthrow/actions?query=workflow%3Amain-openjdk_lts-linux) |
| OpenJDK Current | Linux    | [![Build (OpenJDK Current, Linux)](https://img.shields.io/github/workflow/status/io7m/jdeferthrow/main-openjdk_current-linux)](https://github.com/io7m/jdeferthrow/actions?query=workflow%3Amain-openjdk_current-linux)
| OpenJDK Current | Windows  | [![Build (OpenJDK Current, Windows)](https://img.shields.io/github/workflow/status/io7m/jdeferthrow/main-openjdk_current-windows)](https://github.com/io7m/jdeferthrow/actions?query=workflow%3Amain-openjdk_current-windows)

The `jdeferthrow` package implements a trivial API for combining multiple 
exceptions over a series of statements.

### Usage

```
final var tracker = new ExceptionTracker<IOException>();

try {
  doIO1();
} catch (IOException e1) {
  tracker.addException(e1);
}

try {
  doIO2();
} catch (IOException e2) {
  tracker.addException(e2);
}

try {
  doIO3();
} catch (IOException e3) {
  tracker.addException(e3);
}

tracker.throwIfNecessary();
```

The above code will execute `doIO1`, `doIO2`, and `doIO3`, catching each
exception if any are raised. The `throwIfNecessary` method will throw
whichever of `e1`, `e2`, or `e3` was caught first, with any/all of the other
two exceptions added to the thrown exception as a _suppressed exception_.

Concretely, if all of `doIO1`, `doIO2`, and `doIO3` throw exceptions, the
`throwIfNecessary` method will throw `e1` with `e2` and `e3` added to `e1`
as suppressed exceptions.

This effectively allows for accumulating exceptions over a range of statements
and then throwing an exception at the end that contains all of the exceptions
that were thrown.

If `addException` was not called, `throwIfNecessary` does nothing.

