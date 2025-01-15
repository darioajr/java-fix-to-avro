package io.github.darioajr.converter.models;

import io.github.darioajr.converter.core.SchemaProvider;

/**
 * FixCustomVersion.
 */
public class FixCustomVersion implements SchemaProvider {
  private final String version;
  private final String schemaPath;
  
  /**
   * FixVersionConfig constructor.
   */
  public FixCustomVersion(String version, String schemaPath) {
    this.version = version;
    this.schemaPath = schemaPath;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public String getSchemaPath() {
    return schemaPath;
  }
}