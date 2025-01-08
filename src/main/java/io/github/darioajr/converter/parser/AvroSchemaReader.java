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
