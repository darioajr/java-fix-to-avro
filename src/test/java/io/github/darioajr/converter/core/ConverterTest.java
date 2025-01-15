package io.github.darioajr.converter.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import io.github.darioajr.converter.models.FixDefaultVersion;
import io.github.darioajr.converter.utils.AvroUtils;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class ConverterTest {

  private Converter<String, GenericRecord> converter;
  private FixDefaultVersion fixDefaultVersion;

  @BeforeEach
  void setUp() {
    fixDefaultVersion = mock(FixDefaultVersion.class); // Mock do FixVersion
    converter = new Converter<String, GenericRecord>() {
      @Override
      public GenericRecord convertToAvro(String rawMessage, SchemaProvider schema)
          throws IOException {
        return AvroUtils.convertFixToAvro(rawMessage, schema);
      }
    };
  }

  @Test
  void testConvertToAvro() throws IOException {
    try (MockedStatic<AvroUtils> mockedStatic = mockStatic(AvroUtils.class)) {
      String schemaPath = getClass()
          .getClassLoader()
          .getResource("schemas/FIX44.xml").getPath();

      String rawMessage = "8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|"
          + "34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|";

      FixDefaultVersion version = mock(FixDefaultVersion.class);

      when(version.getSchemaPath()).thenReturn(schemaPath);

      GenericRecord mockRecord = mock(GenericRecord.class);

      mockedStatic.when(() -> AvroUtils.convertFixToAvro(rawMessage, version))
          .thenReturn(mockRecord);

      GenericRecord result = converter.convertToAvro(rawMessage, version);

      mockedStatic.verify(() -> AvroUtils.convertFixToAvro(rawMessage, version), 
          times(1));

      assertNotNull(result);
      assertEquals(mockRecord, result);
    }
  }
}