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

package io.github.darioajr.converter.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.avro.Schema;

/**
 * Implementation of AvroSchemaReader.
 * 
 */
public class AvroSchemaReader {
  private static final String AVRO_SCHEMA_PATH = "src/main/resources/schemas/FixMessage.avsc";

  /**
   * Implementation of readAvroSchema.
   * 
   */
  public static Schema readAvroSchema(String schemaPath)
      throws IOException, org.apache.avro.SchemaParseException {
    return new Schema.Parser().parse(Paths.get(schemaPath).toFile());
  }
  
  /**
   * Implementation of readDefaultAvroSchema.
   * 
   */
  public static Schema readDefaultAvroSchema()
      throws IOException, org.apache.avro.SchemaParseException {
    return new Schema.Parser().parse(new File(AVRO_SCHEMA_PATH));
  }
}
