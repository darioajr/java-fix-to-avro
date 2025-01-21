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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.avro.Schema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FixSchemaReaderTest {

  @Test
  void testReadAvroSchema(@TempDir Path tempDir) throws IOException {
    Path schemaPath = tempDir.resolve("tempfile-avro-fix.avsc");
    Files.writeString(schemaPath, """
        {
            "type": "record",
            "name": "FixMessage",
            "fields": [
                { "name": "beginString", "type": "string" },
                { "name": "bodyLength", "type": "string" },
                { "name": "msgType", "type": "string" },
                { "name": "senderCompID", "type": "string" },
                { "name": "targetCompID", "type": "string" },
                { "name": "msgSeqNum", "type": "string" },
                { "name": "sendingTime", "type": "string" },
                { "name": "fields", "type": { "type": "map", "values": "string" } },
                { "name": "checkSum", "type": "string" }
            ]
        }
        """);

    Schema schema = AvroSchemaReader.readAvroSchema(schemaPath.toString());

    assertThat(schema).isNotNull();
    assertThat(schema.getType()).isEqualTo(Schema.Type.RECORD);
    assertThat(schema.getFields())
        .hasSize(9)
        .extracting(Schema.Field::name)
        .containsExactly("beginString", "bodyLength", "msgType", 
          "senderCompID", "targetCompID", "msgSeqNum", "sendingTime", "fields", "checkSum");
  }

  @Test
  void testSchemaTypeMapping(@TempDir Path tempDir) throws IOException {
    Path schemaPath = tempDir.resolve("tempfile-avro-fix.avsc");

    Files.writeString(schemaPath, """
        {
            "type": "record",
            "name": "FixMessage",
            "fields": [
                { "name": "beginString", "type": "string" },
                { "name": "bodyLength", "type": "string" },
                { "name": "msgType", "type": "string" },
                { "name": "senderCompID", "type": "string" },
                { "name": "targetCompID", "type": "string" },
                { "name": "msgSeqNum", "type": "string" },
                { "name": "sendingTime", "type": "string" },
                { "name": "fields", "type": { "type": "map", "values": "string" } },
                { "name": "checkSum", "type": "string" }
            ]
        }
        """);

    Schema schema = AvroSchemaReader.readAvroSchema(schemaPath.toString());

    assertThat(schema.getFields())
        .satisfiesExactly(
            field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),
            field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),
            field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),
            field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),
            field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),
            field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),
            field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),
            field -> {
                assertThat(field.schema().getType()).isEqualTo(Schema.Type.MAP);
                assertThat(field.schema().getValueType().getType())
                  .isEqualTo(Schema.Type.STRING);
            },
            field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING)
        );
  }

  @Test
  void testInvalidSchemaFile(@TempDir Path tempDir) throws IOException {
    Path schemaPath = tempDir.resolve("invalid_schema.txt");
    Files.writeString(schemaPath, "InvalidSchema");

    assertThatThrownBy(() -> AvroSchemaReader.readAvroSchema(schemaPath.toString()))
        .isInstanceOf(org.apache.avro.SchemaParseException.class);
  }
}