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

package io.github.darioajr.converter.integration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.darioajr.converter.core.FixConverter;
import io.github.darioajr.converter.models.FixCustomVersion;
import io.github.darioajr.converter.models.FixDefaultVersion;
import io.github.darioajr.converter.parser.FixMessageParser;
import io.github.darioajr.converter.validation.FixMessageValidator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FixToAvroIntegrationTest {
  private FixConverter converterService;

  @BeforeEach
  void setUp() {
    converterService = new FixConverter();
  }

  @Test
  @DisplayName("Teste de conversão com schema padrão")
  void testDefaultSchemaConversion() {
    String newOrderSingle = "8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|"
        + "34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|";

    GenericRecord record = converterService.convertToAvro(newOrderSingle, 
        FixDefaultVersion.FIX_4_4);

    System.out.println(record);
    assertNotNull(record, "O registro Avro não deve ser nulo.");
    assertEquals("FIX.4.4", record.get("beginString"));
    assertEquals("D", record.get("msgType"));
    @SuppressWarnings("unchecked")
    Map<String, String> fields = (Map<String, String>) record.get("fields"); 
    assertNotNull(fields, "O campo 'fields' não deve ser nulo."); 
    String order123 = fields.get("11"); 
    assertEquals("Order123", order123);
  }

  @Test
  @DisplayName("Teste de conversão com schema personalizado")
  void testCustomSchemaConversion() {
    FixCustomVersion fixCustomVersion = new FixCustomVersion(FixDefaultVersion.FIX_4_4.getVersion(),
        getClass()
          .getClassLoader()
          .getResource("schemas/FIX44_custom.xml")
          .getPath());

    String newOrderSingleCustom = "8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|"
         + "34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|";

    GenericRecord record = converterService.convertToAvro(newOrderSingleCustom, 
        fixCustomVersion);

    assertNotNull(record, "O registro Avro não deve ser nulo.");
    assertEquals("FIX.4.4", record.get("beginString"));
    assertEquals("XX", record.get("msgType"));
    @SuppressWarnings("unchecked")
    Map<String, String> fields = (Map<String, String>) record.get("fields"); 
    assertNotNull(fields, "O campo 'fields' não deve ser nulo."); 
    String order123 = fields.get("11"); 
    assertEquals("Order123", order123);
  }

  @Test
  @DisplayName("Teste de conversão com validação e schema personalizado")
  void testCustomSchemaConversionWithValidation() {
    String newOrderSingleCustom = "8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|"
        + "34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|";
    
    FixMessageParser parser = new FixMessageParser();
    
    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("8", "FIX.4.4");
    fieldCriteria.put("35", Arrays.asList("D", "G"));
    fieldCriteria.put("54", Arrays.asList("1", "2"));

    FixMessageValidator validator = new FixMessageValidator();

    Map<String, String> parsedFields = parser.parse(newOrderSingleCustom, 
        FixDefaultVersion.FIX_4_4);

    assertDoesNotThrow(() -> {
      validator.validateFields(parsedFields, FixDefaultVersion.FIX_4_4, fieldCriteria); 
    }, "A validação da mensagem FIX falhou.");

    FixCustomVersion fixCustomVersion = new FixCustomVersion(FixDefaultVersion.FIX_4_4.getVersion(),
        getClass()
         .getClassLoader()
         .getResource("schemas/FIX44_custom.xml")
         .getPath());

    GenericRecord record = converterService.convertToAvro(newOrderSingleCustom,
        fixCustomVersion);

    assertNotNull(record, "O registro Avro não deve ser nulo.");
    assertEquals("FIX.4.4", record.get("beginString"));
    assertEquals("D", record.get("msgType"));
    @SuppressWarnings("unchecked")
    Map<String, String> fields = (Map<String, String>) record.get("fields"); 
    assertNotNull(fields, "O campo 'fields' não deve ser nulo."); 
    String order123 = fields.get("11"); 
    assertEquals("Order123", order123);
  }

  @AfterEach
  void tearDown() {
    
  }
}
