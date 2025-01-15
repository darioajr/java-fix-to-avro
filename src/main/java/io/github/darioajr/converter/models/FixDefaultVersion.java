package io.github.darioajr.converter.models;

import io.github.darioajr.converter.core.SchemaProvider;
import java.util.Objects;

/**
 * Implementation of FixVersion.
 */
public enum FixDefaultVersion implements SchemaProvider {
  FIX_4_4("44", "schemas/FIX44.xml"),
  FIX_5_0("50", "schemas/FIX50.xml"),
  FIX_5_0_SP1("50SP1", "schemas/FIX50SP1.xml"),
  FIX_5_0_SP2("50SP2", "schemas/FIX50SP2.xml");

  private final String version;
  private final String schemaPath;
  
  /**
   * Implementation of custom Constructor.
   */
  FixDefaultVersion(String version, String schemaPath) {
    this.version = version;
    this.schemaPath = schemaPath;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public String getSchemaPath() {
    return Objects.requireNonNull(
      getClass().getClassLoader().getResource(schemaPath)).getPath();
  }
}