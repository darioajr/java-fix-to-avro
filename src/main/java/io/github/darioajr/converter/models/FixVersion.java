package io.github.darioajr.converter.models;

import java.nio.file.Path;

/**
 * Implementation of FixVersion.
 */
public enum FixVersion {
  FIX_4_4("44", "src/main/resources/schemas/FIX44.xml"),
  FIX_5_0("50", "src/main/resources/schemas/FIX50.xml"),
  FIX_5_0_SP1("50SP1", "src/main/resources/schemas/FIX50SP1.xml"),
  FIX_5_0_SP2("50SP2", "src/main/resources/schemas/FIX50SP2.xml");

  private final String version;
  private final String defaultSchemaPath;
  private Path customSchemaPath;
    
  FixVersion() {
    this.version = "44";
    this.defaultSchemaPath = "src/main/resources/schemas/FIX44.xml";
  }

  FixVersion(String version, String defaultSchemaPath) {
    this.version = version;
    this.defaultSchemaPath = defaultSchemaPath;
  }

  public String getVersion() {
    return version;
  }

  public String getSchemaPath() {
    return customSchemaPath != null ? customSchemaPath.toString() : defaultSchemaPath;
  }

  public void setCustomSchemaPath(Path customSchemaPath) {
    this.customSchemaPath = customSchemaPath;
  }

  /**
   * Implementation of hasCustomSchema.
   */
  public boolean hasCustomSchema() {
    return customSchemaPath != null;
  }
}