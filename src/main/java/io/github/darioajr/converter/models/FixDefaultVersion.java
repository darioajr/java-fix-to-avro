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
import java.util.Objects;

/**
 * Enum representing default FIX versions with their associated schema paths.
 */
public enum FixDefaultVersion implements SchemaProvider {
  /**
   * FIX 4.4 version with schema path.
   */
  FIX_4_4("44", "schemas/FIX44.xml"),

  /**
   * FIX 5.0 version with schema path.
   */
  FIX_5_0("50", "schemas/FIX50.xml"),
  
  /**
   * FIX 5.0 SP1 version with schema path.
   */
  FIX_5_0_SP1("50SP1", "schemas/FIX50SP1.xml"),
  
  /**
   * FIX 5.0 SP2 version with schema path.
   */
  FIX_5_0_SP2("50SP2", "schemas/FIX50SP2.xml");

  private final String version;
  private final String schemaPath;
  
  /**
   * Constructs a FixDefaultVersion with the specified version and schema path.
   *
   * @param version the version of the FIX protocol
   * @param schemaPath the path to the schema file
   */
  FixDefaultVersion(String version, String schemaPath) {
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
    return Objects.requireNonNull(
      getClass().getClassLoader().getResource(schemaPath)).getPath();
  }
}