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

import io.github.darioajr.converter.utils.AvroUtils;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;

/**
 * Implementation of FixConverter.
 */
public class FixConverter implements Converter<String, GenericRecord> {
  /**
   * Implementation of FixConverter Constructor.
   */
  public FixConverter() {}

  @Override
  public GenericRecord convertToAvro(String rawMessage, SchemaProvider schema) {
    return AvroUtils.convertFixToAvro(rawMessage, schema);
  }

  /**
   * Converts a FIX message to an Avro byte array.
   *
   * @param rawMessage the raw FIX message as a string
   * @param schema the schema provider for the Avro schema
   * @return the serialized Avro byte array
   * @throws IOException if an I/O error occurs during conversion or serialization
   */
  public byte[] convertToByteArray(String rawMessage, SchemaProvider schema) throws IOException {
    return AvroUtils.convertFixToAvroByteArray(rawMessage, schema);
  }
}