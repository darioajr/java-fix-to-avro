/**
 * Copyright 2025 Dario Alves Junior. All Rights Reserved.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

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
   * Converts the input message to an Avro format.
   *
   * @param rawMessage the raw input message
   * @param schema the schema provider for the Avro schema
   * @return the converted message in Avro format
   * @throws IOException if an I/O error occurs during conversion
   */
  O convertToAvro(I rawMessage, SchemaProvider schema) throws IOException;
}
