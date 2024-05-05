/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jdeferthrow.tests;

import com.io7m.jdeferthrow.core.ExceptionTracker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link ExceptionTracker}.
 */

public final class ExceptionTrackerTest
{
  /**
   * Tests for {@link ExceptionTracker}.
   */

  public ExceptionTrackerTest()
  {

  }

  /**
   * If no exceptions are logged, nothing is thrown.
   *
   * @throws Exception On errors
   */

  @Test
  public void test_ThrowIfRequired_NoExceptions_NothingHappens()
    throws Exception
  {
    final var tracker = new ExceptionTracker<>();
    tracker.throwIfNecessary();
  }

  /**
   * If exceptions are logged, the thrown exception is the first exception
   * with the rest available as suppressed exceptions.
   *
   * @throws Exception On errors
   */

  @Test
  public void test_ThrowIfRequired_MoreExceptions_FirstThrown()
    throws Exception
  {
    final var tracker = new ExceptionTracker<Exception>();

    final var ex0 =
      new IOException("Exception 0");
    final var ex1 =
      new IllegalStateException("Exception 1");
    final var ex2 =
      new InterruptedException("Exception 2");

    tracker.addException(ex0);
    tracker.addException(ex1);
    tracker.addException(ex2);

    final var thrown =
      Assertions.assertThrows(Exception.class, tracker::throwIfNecessary);

    assertEquals(ex0, thrown);
    assertEquals(ex0.getSuppressed()[0], ex1);
    assertEquals(ex0.getSuppressed()[1], ex2);
    assertEquals(2, ex0.getSuppressed().length);
  }

  /**
   * If exceptions are caught, the thrown exception is the first exception
   * with the rest available as suppressed exceptions.
   *
   * @throws Exception On errors
   */

  @Test
  public void test_Catching_Exceptions_AllAreCaught()
    throws Exception
  {
    final var tracker = new ExceptionTracker<Exception>();

    final var ex0 =
      new IOException("Exception 0");
    final var ex1 =
      new IllegalStateException("Exception 1");
    final var ex2 =
      new InterruptedException("Exception 2");

    tracker.catching(() -> {
      throw ex0;
    });
    tracker.catching(() -> {
      throw ex1;
    });
    tracker.catching(() -> {
      throw ex2;
    });
    tracker.catching(() -> {
      // Do nothing!
    });

    final var thrown =
      Assertions.assertThrows(Exception.class, tracker::throwIfNecessary);

    assertEquals(ex0, thrown);
    assertEquals(ex0.getSuppressed()[0], ex1);
    assertEquals(ex0.getSuppressed()[1], ex2);
    assertEquals(2, ex0.getSuppressed().length);
  }
}
