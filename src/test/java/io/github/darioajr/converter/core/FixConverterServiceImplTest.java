package io.github.darioajr.converter.core;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.darioajr.converter.models.FixDefaultVersion;
import io.github.darioajr.converter.utils.AvroUtils;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class FixConverterServiceImplTest {

  private FixConverter fixConverter;
  private FixDefaultVersion fixVersion;
    
  @BeforeEach
  void setUp() {
    fixConverter = new FixConverter();
    fixVersion = mock(FixDefaultVersion.class); // Mock do FixVersion
        
    // Injetar dependências mockadas (caso necessário, mas neste caso não há injeção explícita)
  }
/* 
  @Test
  void testConfigureCustomSchema() {
    String schemaPath = getClass()
        .getClassLoader()
        .getResource("schemas/FIX44_custom.xml").getPath();

    // Chama o método
    fixConverter.configureCustomSchema(fixVersion, schemaPath);

    // Verifica se o método setCustomSchemaPath foi chamado no fixVersion
    verify(fixVersion, times(1)).setSchemaPath(schemaPath);
  }

  @Test
  void testConvertFixToAvro() throws IOException {
    try (MockedStatic<AvroUtils> mockedStatic = mockStatic(AvroUtils.class)) {
      String rawMessage = """
        8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|34=1|
        52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|
          """;
      FixVersion version = mock(FixVersion.class);
      when(version.getSchemaPath()).thenReturn("src/test/resources/schemas/FIX44.xml");
      GenericRecord mockRecord = mock(GenericRecord.class);

      mockedStatic.when(() -> AvroUtils.convertFixToAvro(rawMessage,
        version)).thenReturn(mockRecord);

      GenericRecord result = fixConverterService.convertFixToAvro(rawMessage, version);

      mockedStatic.verify(() -> AvroUtils.convertFixToAvro(rawMessage, version), times(1));
      assertNotNull(result);
      assertEquals(mockRecord, result);
    }
  }

  @Test
  void testConvertFixToAvroByteArray() throws IOException {
    try (MockedStatic<AvroUtils> mockedStatic = mockStatic(AvroUtils.class)) {
      String rawMessage = """
        8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|
        52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|
          """;
      FixVersion version = mock(FixVersion.class);  // Mock do FixVersion
      byte[] mockByteArray = new byte[]{1, 2, 3, 4};

      // Mock da função AvroUtils.convertFixToAvroByteArray
      when(AvroUtils.convertFixToAvroByteArray(rawMessage, version)).thenReturn(mockByteArray);

      // Chama o método
      byte[] result = fixConverterService.convertFixToAvroByteArray(rawMessage, version);

      // Verifica se o método foi chamado e se o retorno é o mock esperado
      verify(AvroUtils.class, times(1));
      AvroUtils.convertFixToAvroByteArray(rawMessage, version);
      assertNotNull(result);
      assertArrayEquals(mockByteArray, result);
    }
  }
    */
}
