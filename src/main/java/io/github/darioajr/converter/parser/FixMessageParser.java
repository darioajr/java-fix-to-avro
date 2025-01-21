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

package io.github.darioajr.converter.parser;

import io.github.darioajr.converter.core.SchemaProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of FixMessageParser.
 * 
 */
public class FixMessageParser {

  private static final char SOH_DELIMITER = '\u0001'; // SOH delimiter character
  private static final String KEY_VALUE_SEPARATOR = "=";

  /**
   * Default constructor.
   * This constructor is intentionally empty. Nothing special is needed here.
   */
  public FixMessageParser() {
    // Default constructor
  }
  
  /**
   * Parses a FIX message into a map of key-value pairs.
   *
   * @param fixMessage The FIX message as a string.
   * @param schema    The FIX version used for specific validations (optional).
   * @return A map containing the fields of the FIX message (tag, value).
   * @throws IllegalArgumentException If the message is null or invalid.
   */
  public Map<String, String> parse(String fixMessage, SchemaProvider schema) {
    validateMessage(fixMessage);

    // Replace the vertical bar delimiter with the SOH delimiter
    fixMessage = fixMessage.replace('|', SOH_DELIMITER);

    // Map to store the key-value pairs of the message
    Map<String, String> parsedFields = new HashMap<>();
    // Split the message into fields
    String[] fields = fixMessage.split(String.valueOf(SOH_DELIMITER)); 

    for (String field : fields) {
      parseField(field, parsedFields);
    }

    return parsedFields;
  }

  /**
   * Validates if the FIX message is valid.
   *
   * @param fixMessage The FIX message to validate.
   * @throws IllegalArgumentException If the message is null or empty.
   */
  private void validateMessage(String fixMessage) {
    if (fixMessage == null || fixMessage.trim().isEmpty()) {
      throw new IllegalArgumentException("The FIX message cannot be null or empty.");
    }
  }

  /**
   * Parses a FIX field and adds it to the map of fields.
   *
   * @param field        The FIX field in key-value format (e.g., "35=D").
   * @param parsedFields The map to store the parsed fields.
   */
  private void parseField(String field, Map<String, String> parsedFields) {
    if (field == null || field.isEmpty()) {
      return; // Ignore empty fields
    }

    String[] keyValue = field.split(KEY_VALUE_SEPARATOR, 2); // Split at the first "=" found

    if (keyValue.length == 2) {
      String tag = keyValue[0].trim();
      String value = keyValue[1].trim();

      // Only add to the map if both values are valid
      if (!tag.isEmpty() && !value.isEmpty()) {
        parsedFields.put(tag, value);
      }
    }
  }
}
