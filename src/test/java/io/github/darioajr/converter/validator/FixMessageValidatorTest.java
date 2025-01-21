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

package io.github.darioajr.converter.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.darioajr.converter.models.FixDefaultVersion;
import io.github.darioajr.converter.parser.FixMessageParser;
import io.github.darioajr.converter.validation.FixMessageValidator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FixMessageValidatorTest {

  @Test
  void validateFields_withValidFields_shouldPass() {
    String newOrderSingleCustom = """
      8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|
        """;

    FixMessageParser fixMessageParser = new FixMessageParser();

    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("8", "FIX.4.4");
    fieldCriteria.put("35", Arrays.asList("D", "XX"));
    fieldCriteria.put("54", Arrays.asList("1", "2"));

    Map<String, String> parsedFields = fixMessageParser.parse(newOrderSingleCustom, 
        FixDefaultVersion.FIX_4_4);

    FixMessageValidator validator = new FixMessageValidator();

    validator.validateFields(parsedFields, FixDefaultVersion.FIX_4_4, fieldCriteria);
  }

  @Test
  void validateFields_withMissingField_shouldThrowException() {
    String newOrderSingleCustom = """
      8=FIX.4.4|35=XX|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|
        """;

    FixMessageParser parser = new FixMessageParser();
    
    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("8", "FIX.4.4");
    fieldCriteria.put("9", null);
    fieldCriteria.put("54", Arrays.asList("1", "2"));

    FixMessageValidator validator = new FixMessageValidator();

    Map<String, String> parsedFields = parser.parse(newOrderSingleCustom,
        FixDefaultVersion.FIX_4_4);

    assertThatThrownBy(() -> 
      validator.validateFields(parsedFields, FixDefaultVersion.FIX_4_4, fieldCriteria)
    ).isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("The required field is missing: tag 9");
  }

  @Test
  void validateFields_withInvalidFieldValue_shouldThrowException() {
    String newOrderSingleCustom = """
      8=FIX.4.9|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|
        """;

    FixMessageParser parser = new FixMessageParser();

    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("8", "FIX.4.4");
    fieldCriteria.put("35", Arrays.asList("D", "XX"));
    fieldCriteria.put("54", Arrays.asList("1", "2"));

    FixMessageValidator validator = new FixMessageValidator();

    Map<String, String> parsedFields = parser.parse(newOrderSingleCustom,
        FixDefaultVersion.FIX_4_4);
    
    assertThatThrownBy(() -> 
      validator.validateFields(parsedFields, FixDefaultVersion.FIX_4_4, fieldCriteria)
    ).isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("FIX message incompatible with version FIX.4.4.");
  }

  @Test
  void validateFields_withExtraField_shouldPass() {
    String newOrderSingleCustom = """
      8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|52=20231208-12:34:56|11=Order123|
      54=1|38=100|55=AAPL|44=50.00|999=TESTE|10=94|
         """;
    
    FixMessageParser parser = new FixMessageParser();
    
    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("8", "FIX.4.4");
    fieldCriteria.put("35", Arrays.asList("D", "XX"));
    fieldCriteria.put("54", Arrays.asList("1", "2"));
    fieldCriteria.put("999", "TESTE");

    FixMessageValidator validator = new FixMessageValidator();
    Map<String, String> parsedFields = parser.parse(newOrderSingleCustom, 
        FixDefaultVersion.FIX_4_4);

    validator.validateFields(parsedFields, FixDefaultVersion.FIX_4_4, fieldCriteria);
  }
}
