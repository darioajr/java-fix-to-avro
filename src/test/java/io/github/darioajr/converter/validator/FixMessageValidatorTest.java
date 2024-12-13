package io.github.darioajr.converter.validator;

import io.github.darioajr.converter.models.FixVersion;
import io.github.darioajr.converter.parser.FixMessageParser;
import io.github.darioajr.converter.validation.FixMessageValidator;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FixMessageValidatorTest {

   // private final FixMessageValidator validator = new FixMessageValidator();

    @Test
    void validateFields_withValidFields_shouldPass() {

        // Mensagem FIX com campos válidos
        String newOrderSingleCustom = "8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|";

        // Criando instância do validor de mensagens FIX
        FixMessageValidator validator = new FixMessageValidator();
        FixMessageParser parser = new FixMessageParser();

        // Parser para obter os campos da mensagem
        Map<String, String> parsedFields = parser.parse(newOrderSingleCustom, FixVersion.FIX_4_4);

        // Critérios de validação: tag=valor esperado (String ou List<String>)
        Map<String, Object> fieldCriteria = new HashMap<>();
        fieldCriteria.put("8", "FIX.4.4"); // Verifica versão
        fieldCriteria.put("35", Arrays.asList("D", "XX")); // MsgType deve ser "D" ou "XX"
        fieldCriteria.put("54", Arrays.asList("1", "2")); // Side deve ser "1" ou "2"

        validator.validateFields(parsedFields, FixVersion.FIX_4_4, fieldCriteria);
        // Nenhuma exceção lançada significa que o teste passou
    }

    @Test
    void validateFields_withMissingField_shouldThrowException() {
       // Mensagem FIX com campos válidos
       String newOrderSingleCustom = "8=FIX.4.4|35=XX|49=SenderCompID|56=TargetCompID|34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|";

       // Criando instância do validor de mensagens FIX
       FixMessageValidator validator = new FixMessageValidator();
       FixMessageParser parser = new FixMessageParser();

       // Parser para obter os campos da mensagem
       Map<String, String> parsedFields = parser.parse(newOrderSingleCustom, FixVersion.FIX_4_4);

       // Critérios de validação: tag=valor esperado (String ou List<String>)
       Map<String, Object> fieldCriteria = new HashMap<>();
       fieldCriteria.put("8", "FIX.4.4"); // Verifica versão
       fieldCriteria.put("9", null); // Deve existir"
       fieldCriteria.put("54", Arrays.asList("1", "2")); // Side deve ser "1" ou "2"

        assertThatThrownBy(() -> 
            validator.validateFields(parsedFields, FixVersion.FIX_4_4, fieldCriteria)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("O campo obrigatório está ausente: tag 9");
    }

    @Test
    void validateFields_withInvalidFieldValue_shouldThrowException() {
       // Mensagem FIX com campos válidos
       String newOrderSingleCustom = "8=FIX.4.9|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|";

       // Criando instância do validor de mensagens FIX
       FixMessageValidator validator = new FixMessageValidator();
       FixMessageParser parser = new FixMessageParser();

       // Parser para obter os campos da mensagem
       Map<String, String> parsedFields = parser.parse(newOrderSingleCustom, FixVersion.FIX_4_4);

       // Critérios de validação: tag=valor esperado (String ou List<String>)
       Map<String, Object> fieldCriteria = new HashMap<>();
       fieldCriteria.put("8", "FIX.4.4"); // Verifica versão
       fieldCriteria.put("35", Arrays.asList("D", "XX")); // MsgType deve ser "D" ou "XX"
       fieldCriteria.put("54", Arrays.asList("1", "2")); // Side deve ser "1" ou "2"

        assertThatThrownBy(() -> 
            validator.validateFields(parsedFields, FixVersion.FIX_4_4, fieldCriteria)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Mensagem FIX incompatível com a versão FIX.4.4.");
    }

    @Test
    void validateFields_withExtraField_shouldPass() {
       // Mensagem FIX com campos válidos
       String newOrderSingleCustom = "8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|999=TESTE|10=94|";

       // Criando instância do validor de mensagens FIX
       FixMessageValidator validator = new FixMessageValidator();
       FixMessageParser parser = new FixMessageParser();

       // Parser para obter os campos da mensagem
       Map<String, String> parsedFields = parser.parse(newOrderSingleCustom, FixVersion.FIX_4_4);

       // Critérios de validação: tag=valor esperado (String ou List<String>)
       Map<String, Object> fieldCriteria = new HashMap<>();
       fieldCriteria.put("8", "FIX.4.4"); // Verifica versão
       fieldCriteria.put("35", Arrays.asList("D", "XX")); // MsgType deve ser "D" ou "XX"
       fieldCriteria.put("54", Arrays.asList("1", "2")); // Side deve ser "1" ou "2"
       fieldCriteria.put("999", "TESTE"); // Verifica campo extra

        validator.validateFields(parsedFields, FixVersion.FIX_4_4, fieldCriteria);
        // Nenhuma exceção lançada significa que o teste passou
    }
}
