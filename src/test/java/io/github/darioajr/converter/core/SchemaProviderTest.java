/**
 * Copyright 2025 Dario Alves Junior. All Rights Reserved.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

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
