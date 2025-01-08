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
