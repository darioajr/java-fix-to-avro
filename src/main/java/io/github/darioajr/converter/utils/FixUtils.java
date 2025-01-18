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

package io.github.darioajr.converter.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import quickfix.Field;
import quickfix.Message;

/**
 * Implementation of FixUtils.
 * 
 */
public class FixUtils {

  /**
   * Implementation of getFieldsAsMap.
   * 
   */
  public static Map<String, String> getFieldsAsMap(Message message) {
    Map<String, String> fieldMap = new HashMap<>();

    for (Iterator<Field<?>> it = message.iterator(); it.hasNext();) {
      Field<?> field = it.next();
      int tag = field.getTag();
      String value = field.getObject().toString();
      fieldMap.put(String.valueOf(tag), value);
    }
    return fieldMap;
  }
}
