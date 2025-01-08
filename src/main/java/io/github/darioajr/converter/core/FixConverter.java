package io.github.darioajr.converter.core;

import io.github.darioajr.converter.models.FixVersion;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;

/**
 * Implementation of FixConverter.
 */
public interface FixConverter {
  /**
   * Implementation of convertFixToAvro.
   */
  GenericRecord convertFixToAvro(String rawMessage, FixVersion version) throws IOException;
  
  /**
   * Implementation of convertFixToAvroByteArray.
   */
  byte[] convertFixToAvroByteArray(String rawMessage, FixVersion version) throws IOException;
}
