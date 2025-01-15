package io.github.darioajr.converter.core;

/**
 * Simple implementation of SchemaProvider for testing purposes.
 */
public class SimpleSchemaProvider implements SchemaProvider {
  private final String version;
  private final String schemaPath;

  /**
   * Constructs a SimpleSchemaProvider with the specified version and schema path.
   *
   * @param version the version of the schema
   * @param schemaPath the path to the schema
   */
  public SimpleSchemaProvider(String version, String schemaPath) {
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
