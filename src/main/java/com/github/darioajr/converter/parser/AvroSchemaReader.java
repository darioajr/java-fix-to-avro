package com.github.darioajr.converter.parser;

import com.github.darioajr.converter.models.FixVersion;
import org.apache.avro.Schema;

import java.io.IOException;
import java.io.File;

public class AvroSchemaReader {
    private static final String AVRO_SCHEMA_PATH = "src/main/resources/schemas/fix_message.avsc";

    public Schema readAvroSchema(String schemaPath, FixVersion version) throws IOException {
        return new Schema.Parser().parse(new File(AVRO_SCHEMA_PATH));
    }
}
