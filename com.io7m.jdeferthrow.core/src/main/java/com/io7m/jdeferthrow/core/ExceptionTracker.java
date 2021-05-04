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

package com.io7m.jdeferthrow.core;

import java.util.Objects;

/**
 * A class that tracks exceptions. Each call to {@link #addException(Exception)}
 * records an exception in the class, and any call to {@link #throwIfNecessary()}
 * afterwards will throw an exception that combines all of the added exceptions
 * as suppressed exceptions.
 *
 * @param <T> The type of exceptions raised
 */

public final class ExceptionTracker<T extends Exception>
{
  private T exception;

  /**
   * A class that tracks exceptions.
   */

  public ExceptionTracker()
  {

  }

  /**
   * Add an exception to the tracker.
   *
   * @param nextException The exception
   */

  public void addException(
    final T nextException)
  {
    Objects.requireNonNull(nextException, "exception");

    if (this.exception == null) {
      this.exception = nextException;
    } else {
      this.exception.addSuppressed(nextException);
    }
  }

  /**
   * Throw an exception of {@link #addException(Exception)} has been called
   * at least once.
   *
   * @throws T If required
   */

  public void throwIfNecessary()
    throws T
  {
    if (this.exception != null) {
      throw this.exception;
    }
  }
}
