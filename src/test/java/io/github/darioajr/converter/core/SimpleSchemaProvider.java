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
