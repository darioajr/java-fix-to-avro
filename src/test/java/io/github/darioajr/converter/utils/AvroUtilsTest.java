package io.github.darioajr.converter.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;

import io.github.darioajr.converter.models.FixVersion;
import io.github.darioajr.converter.parser.AvroSchemaReader;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;


/**
 * Implementation of AvroUtilsTest.
 * 
 */
class AvroUtilsTest {

  @Test
  void convertFixToAvro_withValidMessage_shouldReturnGenericRecord() {
    String rawMessage = """
      8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|
        """;

    GenericRecord record = AvroUtils.convertFixToAvro(rawMessage, FixVersion.FIX_4_4);

    assertThat(record).isNotNull();
    assertThat(record.get("beginString")).isEqualTo("FIX.4.4");
    assertThat(record.get("msgType")).isEqualTo("XX");
    assertThat(record.get("senderCompID")).isEqualTo("SenderCompID");
    assertThat(record.get("targetCompID")).isEqualTo("TargetCompID");
    assertThat(record.get("msgSeqNum")).isEqualTo("1");
  }

  @Test
  void convertFixToAvro_withInvalidMessage_shouldThrowException() {
    String invalidMessage = "InvalidFIXMessage";

    assertThatThrownBy(() -> 
        AvroUtils.convertFixToAvro(invalidMessage, FixVersion.FIX_4_4)
    ).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Erro ao converter mensagem FIX para Avro");
  }

  @Test
  void convertFixToAvroByteArray_withValidMessage_shouldReturnByteArray() throws IOException {
    String rawMessage = """
      8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|
        """;

    byte[] avroBytes = AvroUtils.convertFixToAvroByteArray(rawMessage, FixVersion.FIX_4_4);
    
    assertThat(avroBytes).isNotNull();
    assertThat(avroBytes.length).isGreaterThan(0);
  }

  @Test
  void convertFixToAvroByteArray_withIoException_shouldThrowException() throws IOException {
    String rawMessage = "8=FIX.4.4|35=D|49=SENDER|56=TARGET|34=1|52=20240210-12:30:00|10=003|";

    try (MockedStatic<AvroSchemaReader> schemaReaderMock = mockStatic(AvroSchemaReader.class)) {
      schemaReaderMock.when(() -> AvroSchemaReader.readDefaultAvroSchema())
          .thenThrow(new IOException("Erro ao ler schema Avro"));

      assertThatThrownBy(() -> 
        AvroUtils.convertFixToAvroByteArray(rawMessage, FixVersion.FIX_4_4)
      ).isInstanceOf(RuntimeException.class)
      .hasMessageContaining("Erro ao converter mensagem FIX para Avro");
    }
  }
}
