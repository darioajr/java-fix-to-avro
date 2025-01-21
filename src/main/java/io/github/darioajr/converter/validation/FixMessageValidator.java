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

package io.github.darioajr.converter.validation;

import io.github.darioajr.converter.core.SchemaProvider;
import java.util.List;
import java.util.Map;

/**
 * Implementation of FixMessageValidator.
 * 
 */
public class FixMessageValidator {

  /**
   * Default constructor.
   * This constructor is intentionally empty. Nothing special is needed here.
   */
  public FixMessageValidator() {
    // Default constructor
  }

  /**
   * Validates the fields of a FIX message based on the provided criteria.
   *
   * @param parsedFields  Fields of the FIX message (key: tag, value: field value).
   * @param schema       FIX version of the message.
   * @param fieldCriteria Validation criteria (key: tag, value: String or List of String).
   */
  public void validateFields(Map<String, String> parsedFields,
      SchemaProvider schema, Map<String, Object> fieldCriteria) {
    if (parsedFields == null || parsedFields.isEmpty()) {
      throw new IllegalArgumentException("The FIX message cannot be empty.");
    }

    // Version validations
    validateVersion(parsedFields, schema);

    // Field validations based on criteria
    for (Map.Entry<String, Object> criterion : fieldCriteria.entrySet()) {
      String tag = criterion.getKey();
      Object expectedValue = criterion.getValue();

      if (!parsedFields.containsKey(tag)) {
        throw new IllegalArgumentException("The required field is missing: tag " + tag);
      }

      String actualValue = parsedFields.get(tag);

      // Check if the expected value is a single value or a list
      if (expectedValue instanceof String) {
        if (!expectedValue.equals(actualValue)) {
          throw new IllegalArgumentException(String.format(
            "The field %s has an invalid value: expected=%s, actual=%s",
            tag, expectedValue, actualValue));
        }
      } else if (expectedValue instanceof List<?>) {
        List<?> allowedValues = (List<?>) expectedValue;
        if (!allowedValues.isEmpty() && allowedValues.get(0) instanceof String) {
          if (!allowedValues.contains(actualValue)) {
            throw new IllegalArgumentException(String.format(
       "The field %s has an invalid value: expected one of=%s, actual=%s",
              tag, allowedValues, actualValue));
          }
        }
      } else {
        throw new IllegalArgumentException("Invalid validation criterion for tag " + tag);
      }
    }
  }

  /**
   * Checks if the FIX message is compatible with the provided version.
   *
   * @param parsedFields Fields of the FIX message.
   * @param schema      Expected FIX version.
   */
  public void validateVersion(Map<String, String> parsedFields, SchemaProvider schema) {
    // Tag BeginString indicates the FIX version
    String beginString = parsedFields.get("8");

    if (beginString == null) {
      throw new IllegalArgumentException(
        "The FIX message does not contain the BeginString tag (8).");
    }

    switch (schema.getVersion()) {
      case "44":
        if (!"FIX.4.4".equals(beginString)) {
          throw new IllegalArgumentException("FIX message incompatible with version FIX.4.4.");
        }
        break;
      case "50":
        if (!"FIX.5.0".equals(beginString)) {
          throw new IllegalArgumentException(
            "FIX message incompatible with version FIX.5.0.");
        }
        break;
      case "50SP1":
        if (!"FIX.5.0SP1".equals(beginString)) {
          throw new IllegalArgumentException("FIX message incompatible with version FIX.5.0SP1.");
        }
        break;
      case "50SP2":
        if (!"FIX.5.0SP2".equals(beginString)) {
          throw new IllegalArgumentException("FIX message incompatible with version FIX.5.0SP2.");
        }
        break;
      default:
        throw new IllegalArgumentException("Unknown FIX version: " + schema);
    }
  }
}
