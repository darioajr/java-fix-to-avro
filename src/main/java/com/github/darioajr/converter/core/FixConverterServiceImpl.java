package com.github.darioajr.converter;

import com.fixtoavro.models.FixVersion;
import com.fixtoavro.parser.FixMessageParser;
import com.fixtoavro.parser.FixSchemaReader;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class FixConverterServiceImpl implements FixConverterService, FixSchemaConfigurer {
    private final FixSchemaReader schemaReader;
    private final FixMessageParser messageParser;

    public FixConverterServiceImpl() {
        this.schemaReader = new FixSchemaReader();
        this.messageParser = new FixMessageParser();
    }

    @Override
    public void configureCustomSchema(FixVersion version, Path schemaPath) {
        version.setCustomSchemaPath(schemaPath);
    }

    @Override
    public void resetToDefaultSchema(FixVersion version) {
        version.setCustomSchemaPath(null);
    }

    @Override
    public GenericRecord convertFixToAvro(String fixMessage, FixVersion version) {
        try {
            // Usa o schema configurado (customizado ou padrão)
            Map<String, String> parsedFields = messageParser.parse(fixMessage, version);
            Schema avroSchema = generateAvroSchema(version.getSchemaPath(), version);

            GenericRecord record = new GenericData.Record(avroSchema);
            parsedFields.forEach(record::put);

            return record;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter mensagem FIX para Avro", e);
        }
    }

    @Override
    public Schema generateAvroSchema(String fixSchemaPath, FixVersion version) throws IOException {
        return schemaReader.readFixSchema(fixSchemaPath, version);
    }
}