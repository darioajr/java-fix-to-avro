package io.github.darioajr.converter.core;

import io.github.darioajr.converter.models.FixVersion;
import io.github.darioajr.converter.utils.AvroUtils;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.avro.generic.GenericRecord;

/**
 * Implementation of FixConverterImpl.
 */
public class FixConverterImpl implements FixConverter, FixCustomSchema {
  /**
   * Implementation of FixConverterImpl Constructor.
   */
  public FixConverterImpl() {}

  @Override
  public void configureCustomSchema(FixVersion version, Path schemaPath) {
    version.setCustomSchemaPath(schemaPath);
  }

  @Override
  public void resetToDefaultSchema(FixVersion version) {
    version.setCustomSchemaPath(null);
  }

  @Override
  public GenericRecord convertFixToAvro(String rawMessage, FixVersion version) {
    return AvroUtils.convertFixToAvro(rawMessage, version);
  }

  @Override
  public byte[] convertFixToAvroByteArray(String rawMessage, FixVersion version)
      throws IOException {
    return AvroUtils.convertFixToAvroByteArray(rawMessage, version);
  }
}