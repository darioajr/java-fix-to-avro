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

package io.github.darioajr.converter.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import io.github.darioajr.converter.models.FixDefaultVersion;
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
  public void testConstructor() throws Exception {
    
    AvroUtils avroUtils = new AvroUtils();
    assertNotNull(avroUtils);
  }

  @Test
  void convertFixToAvrowithValidMessageshouldReturnGenericRecord() {
    String rawMessage = "8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|"
        + "34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|";

    GenericRecord record = AvroUtils.convertFixToAvro(rawMessage, FixDefaultVersion.FIX_4_4);

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
        AvroUtils.convertFixToAvro(invalidMessage, FixDefaultVersion.FIX_4_4)
    ).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Error converting FIX message to Avro");
  }

  @Test
  void convertFixToAvroByteArraywithValidMessageshouldReturnByteArray() throws IOException {
    String rawMessage = "8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|"
        + "34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|";

    byte[] avroBytes = AvroUtils.convertFixToAvroByteArray(rawMessage, FixDefaultVersion.FIX_4_4);
    
    assertThat(avroBytes).isNotNull();
    assertThat(avroBytes.length).isGreaterThan(0);
  }

  @Test
  void convertFixToAvroByteArray_withIoException_shouldThrowException() throws IOException {
    String rawMessage = "8=FIX.4.4|35=D|49=SENDER|56=TARGET|34=1|52=20240210-12:30:00|10=003|";

    try (MockedStatic<AvroSchemaReader> schemaReaderMock = mockStatic(AvroSchemaReader.class)) {
      schemaReaderMock.when(() -> AvroSchemaReader.readDefaultAvroSchema())
          .thenThrow(new IOException("Error reading Avro schema"));

      assertThatThrownBy(() -> 
        AvroUtils.convertFixToAvroByteArray(rawMessage, FixDefaultVersion.FIX_4_4)
      ).isInstanceOf(RuntimeException.class)
      .hasMessageContaining("Error converting FIX message to Avro");
    }
  }
}
