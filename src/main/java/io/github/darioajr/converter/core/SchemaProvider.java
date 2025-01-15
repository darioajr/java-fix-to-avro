package io.github.darioajr.converter.core;

/**
 * Interface for providing FIX schema details.
 */
public interface SchemaProvider {
  /**
   * version.
   */
  String getVersion();
  
  /**
   * schemaPath.
   */
  String getSchemaPath();
}