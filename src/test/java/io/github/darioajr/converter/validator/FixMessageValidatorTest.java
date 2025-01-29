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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.darioajr.converter.core.SchemaProvider;
import io.github.darioajr.converter.models.FixDefaultVersion;
import io.github.darioajr.converter.parser.FixMessageParser;
import io.github.darioajr.converter.validation.FixMessageValidator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FixMessageValidatorTest {

  private final FixMessageValidator validator = new FixMessageValidator();

  @Test
  void validateFields_withEmptyParsedFields_shouldThrowException() {
    Map<String, String> parsedFields = Collections.emptyMap();
    SchemaProvider schema = FixDefaultVersion.FIX_4_4;
    Map<String, Object> fieldCriteria = new HashMap<>();

    assertThatThrownBy(() -> validator.validateFields(parsedFields, schema, fieldCriteria))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("The FIX message cannot be empty.");
  }

  @Test
  void validateFields_withNullParsedFields_shouldThrowException() {
    SchemaProvider schema = FixDefaultVersion.FIX_4_4;
    Map<String, Object> fieldCriteria = new HashMap<>();

    assertThatThrownBy(() -> validator.validateFields(null, schema, fieldCriteria))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("The FIX message cannot be empty.");
  }

  @Test
  void validateFields_withMissingRequiredField_shouldThrowException() {
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.4.4");
    SchemaProvider schema = FixDefaultVersion.FIX_4_4;
    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("9", "123");

    assertThatThrownBy(() -> validator.validateFields(parsedFields, schema, fieldCriteria))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("The required field is missing: tag 9");
  }

  @Test
  void validateFields_withInvalidFieldValue_shouldThrowNewException() {
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.4.4");
    parsedFields.put("9", "456");
    SchemaProvider schema = FixDefaultVersion.FIX_4_4;
    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("9", "123");

    assertThatThrownBy(() -> validator.validateFields(parsedFields, schema, fieldCriteria))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("The field 9 has an invalid value: expected=123, actual=456");
  }
  

  @Test
  void validateVersion_withMissingBeginString_shouldThrowException() {
    Map<String, String> parsedFields = new HashMap<>();
    SchemaProvider schema = FixDefaultVersion.FIX_4_4;

    assertThatThrownBy(() -> validator.validateVersion(parsedFields, schema))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("The FIX message does not contain the BeginString tag (8).");
  }

  @Test
  void validateVersion_withIncompatibleVersion_shouldThrowException() {
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.4.3");
    SchemaProvider schema = FixDefaultVersion.FIX_4_4;

    assertThatThrownBy(() -> validator.validateVersion(parsedFields, schema))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("FIX message incompatible with version FIX.4.4.");
  }

  @Test
  void validateVersion_withIncompatible50_version_shouldThrowException() {
    FixMessageValidator fixMessageValidator = new FixMessageValidator();
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.4.4");
    SchemaProvider schema = FixDefaultVersion.FIX_5_0;

    assertThatThrownBy(() -> fixMessageValidator.validateVersion(parsedFields, schema))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("FIX message incompatible with version FIX.5.0.");
  }
  
  @Test
  void validateFields_withValidFields_shouldPass() {
    String newOrderSingleCustom = """
      8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|
        """;

    FixMessageParser fixMessageParser = new FixMessageParser();

    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("8", "FIX.4.4");
    fieldCriteria.put("35", Arrays.asList("D", "XX"));  // Lista de valores permitidos
    fieldCriteria.put("54", Arrays.asList("1", "2"));

    Map<String, String> parsedFields = fixMessageParser.parse(newOrderSingleCustom, 
        FixDefaultVersion.FIX_4_4);

    FixMessageValidator validator = new FixMessageValidator();

    validator.validateFields(parsedFields, FixDefaultVersion.FIX_4_4, fieldCriteria);
  }

  @Test
  void validateVersion_withIncompatible50sp1_version_shouldThrowException() {
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.5.0");
    SchemaProvider schema = FixDefaultVersion.FIX_5_0_SP1;

    assertThatThrownBy(() -> validator.validateVersion(parsedFields, schema))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("FIX message incompatible with version FIX.5.0SP1.");
  }

  @Test
  void validateVersion_withIncompatible50sp2_version_shouldThrowException() {
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.5.0SP1");
    SchemaProvider schema = FixDefaultVersion.FIX_5_0_SP2;

    assertThatThrownBy(() -> validator.validateVersion(parsedFields, schema))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("FIX message incompatible with version FIX.5.0SP2.");
  }

  @Test
  void validateVersion_withUnknownVersion_shouldThrowNewException() {
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.4.4");
    SchemaProvider schema = new SchemaProvider() {
    
      @Override
      public String getVersion() {
        return "unknown";
      }

      @Override
      public String getSchemaPath() {
        return "unknown";
      }
    };

    assertThatThrownBy(() -> validator.validateVersion(parsedFields, schema))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Unknown FIX version: " + schema);
  }

  @Test
  public void testConstructor() throws Exception {
    
    FixMessageValidator fixMessageValidator = new FixMessageValidator();
    assertNotNull(fixMessageValidator);
  }

  @Test
  void validateFields_withInvalidListFieldValue_shouldThrowException() {
    String newOrderSingleCustom = """
      8=FIX.4.4|9=123|35=Z|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=3|38=100|55=AAPL|44=50.00|10=94|
        """;

    FixMessageParser parser = new FixMessageParser();

    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("8", "FIX.4.4");
    fieldCriteria.put("35", Arrays.asList("D", "XX"));  // Apenas esses são permitidos
    fieldCriteria.put("54", Arrays.asList("1", "2"));   // Apenas esses são permitidos

    FixMessageValidator validator = new FixMessageValidator();

    Map<String, String> parsedFields = parser.parse(newOrderSingleCustom,
        FixDefaultVersion.FIX_4_4);

    assertThatThrownBy(() -> 
      validator.validateFields(parsedFields, FixDefaultVersion.FIX_4_4, fieldCriteria)
    ).isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("The field 35 has an invalid value: expected one of=[D, XX], actual=Z");
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
  void validateFields_withInvalidCriterion_shouldThrowException() {
    String newOrderSingleCustom = """
      8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|
        """;

    FixMessageParser parser = new FixMessageParser();

    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("35", 12345); // Valor inválido (esperado String ou Lista)

    FixMessageValidator validator = new FixMessageValidator();

    Map<String, String> parsedFields = parser.parse(newOrderSingleCustom,
        FixDefaultVersion.FIX_4_4);

    assertThatThrownBy(() -> 
        validator.validateFields(parsedFields, FixDefaultVersion.FIX_4_4, fieldCriteria)
    ).isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("Invalid validation criterion for tag 35");
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

  @Test
  void validateVersion_withIncompatibleVersion50_shouldThrowException() {
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.4.4");
    SchemaProvider schema = FixDefaultVersion.FIX_5_0;

    assertThatThrownBy(() -> validator.validateVersion(parsedFields, schema))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("FIX message incompatible with version FIX.5.0.");
  }

  @Test
  void validateVersion_withIncompatibleVersion50Sp1_shouldThrowException() {
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.4.4");
    SchemaProvider schema = FixDefaultVersion.FIX_5_0_SP1;

    assertThatThrownBy(() -> validator.validateVersion(parsedFields, schema))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("FIX message incompatible with version FIX.5.0SP1.");
  }

  @Test
  void validateVersion_withIncompatibleVersion50Sp2_shouldThrowException() {
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.4.4");
    SchemaProvider schema = FixDefaultVersion.FIX_5_0_SP2;

    assertThatThrownBy(() -> validator.validateVersion(parsedFields, schema))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("FIX message incompatible with version FIX.5.0SP2.");
  }

  @Test
  void validateVersion_withUnknownVersion_shouldThrowException() {
    Map<String, String> parsedFields = new HashMap<>();
    parsedFields.put("8", "FIX.4.4");
    SchemaProvider schema = new SchemaProvider() {
        @Override
        public String getVersion() {
            return "unknown";
        }

        @Override
        public String getSchemaPath() {
            return "unknown";
        }
    };

    assertThatThrownBy(() -> validator.validateVersion(parsedFields, schema))
      .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Unknown FIX version: " + schema);
  }
}
