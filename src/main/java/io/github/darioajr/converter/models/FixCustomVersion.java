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

package io.github.darioajr.converter.models;

import io.github.darioajr.converter.core.SchemaProvider;

/**
 * Represents a custom FIX version with its associated schema path.
 */
public class FixCustomVersion implements SchemaProvider {
  private final String version;
  private final String schemaPath;
  
  /**
   * Constructs a new FixCustomVersion with the specified version and schema path.
   *
   * @param version the version of the FIX protocol
   * @param schemaPath the path to the schema file
   */
  public FixCustomVersion(String version, String schemaPath) {
    this.version = version;
    this.schemaPath = schemaPath;
  }

  /**
   * Gets the version of the FIX protocol.
   *
   * @return the version of the FIX protocol
   */
  @Override
  public String getVersion() {
    return version;
  }

  /**
   * Gets the path to the schema file.
   *
   * @return the path to the schema file
   */
  @Override
  public String getSchemaPath() {
    return schemaPath;
  }
}