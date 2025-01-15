package io.github.darioajr.converter.core;

import io.github.darioajr.converter.utils.AvroUtils;
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
}