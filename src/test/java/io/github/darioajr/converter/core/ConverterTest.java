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

  @BeforeEach
  void setUp() {
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