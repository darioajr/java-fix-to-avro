package io.github.darioajr.converter.parser;

import org.apache.avro.Schema;

import java.io.IOException;
import java.nio.file.Paths;
import java.io.File;

public class AvroSchemaReader {
    private static final String AVRO_SCHEMA_PATH = "src/main/resources/schemas/FixMessage.avsc";

    public static Schema readAvroSchema(String schemaPath) throws IOException, org.apache.avro.SchemaParseException {
        return new Schema.Parser().parse(Paths.get(schemaPath).toFile());
    }
    
    public static Schema readDefaultAvroSchema() throws IOException, org.apache.avro.SchemaParseException {
        return new Schema.Parser().parse(new File(AVRO_SCHEMA_PATH));
    }
}
