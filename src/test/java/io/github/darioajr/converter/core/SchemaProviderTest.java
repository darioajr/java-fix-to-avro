package io.github.darioajr.converter.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class for SchemaProvider.
 */
public class SchemaProviderTest {
  /**
   * Test for getVersion method.
   */
  @Test
  void testGetVersion() {
    String schemaPath = getClass()
          .getClassLoader()
          .getResource("schemas/FIX44.xml").getPath();
    SchemaProvider schemaProvider = new SimpleSchemaProvider("FIX.4.4", schemaPath);
    assertEquals("FIX.4.4", schemaProvider.getVersion());
  }

  /**
   * Test for getSchemaPath method.
   */
  @Test
  void testGetSchemaPath() {
    String schemaPath = getClass()
          .getClassLoader()
          .getResource("schemas/FIX44.xml").getPath();
    SchemaProvider schemaProvider = new SimpleSchemaProvider("FIX.4.4", schemaPath);
    assertEquals(schemaPath, schemaProvider.getSchemaPath());
  }
}
