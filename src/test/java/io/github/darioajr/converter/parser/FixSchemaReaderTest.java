package io.github.darioajr.converter.parser;

import org.apache.avro.Schema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

class FixSchemaReaderTest {

    @Test
    void testReadAvroSchema(@TempDir Path tempDir) throws IOException {
        // Cria um arquivo de schema temporário
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

        // Lê o schema
        Schema schema = AvroSchemaReader.readAvroSchema(schemaPath.toString());

        // Verificações
        assertThat(schema).isNotNull();
        assertThat(schema.getType()).isEqualTo(Schema.Type.RECORD);
        assertThat(schema.getFields())
                .hasSize(9)
                .extracting(Schema.Field::name)
                .containsExactly("beginString", "bodyLength", "msgType", "senderCompID", "targetCompID", "msgSeqNum", "sendingTime", "fields", "checkSum" );
    }

    @Test
    void testSchemaTypeMapping(@TempDir Path tempDir) throws IOException {
        // Cria um arquivo de schema temporário com diversos tipos
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

        // Lê o schema
        Schema schema = AvroSchemaReader.readAvroSchema(schemaPath.toString());

        // Verificações de mapeamento de tipos
        assertThat(schema.getFields())
                .satisfiesExactly(
                        field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),  // beginString é STRING
                        field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),  // bodyLength é STRING
                        field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),  // msgType é STRING
                        field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),  // senderCompID é STRING
                        field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),  // targetCompID é STRING
                        field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),  // msgSeqNum é STRING
                        field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING),  // sendingTime é STRING
                        field -> {
                            // Verificação para o campo "fields" que é um mapa
                            assertThat(field.schema().getType()).isEqualTo(Schema.Type.MAP);
                            assertThat(field.schema().getValueType().getType()) // Acessa diretamente o valueType
                                    .isEqualTo(Schema.Type.STRING); // O tipo do valor no MAP é STRING
                        },
                        field -> assertThat(field.schema().getType()).isEqualTo(Schema.Type.STRING)   // checkSum é STRING
                );
    }

    @Test
    void testInvalidSchemaFile(@TempDir Path tempDir) throws IOException {
        // Cria um arquivo de schema inválido
        Path schemaPath = tempDir.resolve("invalid_schema.txt");
        Files.writeString(schemaPath, "InvalidSchema");

        // Verifica tratamento de schema inválido
        assertThatThrownBy(() -> AvroSchemaReader.readAvroSchema(schemaPath.toString()))
                .isInstanceOf(org.apache.avro.SchemaParseException.class);
    }
}