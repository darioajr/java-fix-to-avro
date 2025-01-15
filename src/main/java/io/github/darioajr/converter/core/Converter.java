package io.github.darioajr.converter.core;

import java.io.IOException;

/**
 * Generic interface for message conversion.
 * Supports conversion between various message formats.
 *
 * @param <I> Input message type
 * @param <O> Output message type
 */
public interface Converter<I, O> {
  /**
   * Implementation of convert.
   */
  O convertToAvro(I rawMessage, SchemaProvider schema) throws IOException;
}
