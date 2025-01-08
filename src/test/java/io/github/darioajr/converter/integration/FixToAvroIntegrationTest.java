package io.github.darioajr.converter.integration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.darioajr.converter.core.FixConverterImpl;
import io.github.darioajr.converter.models.FixVersion;
import io.github.darioajr.converter.parser.FixMessageParser;
import io.github.darioajr.converter.validation.FixMessageValidator;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FixToAvroIntegrationTest {
  private FixConverterImpl converterService;

  @BeforeEach
  void setUp() {
    converterService = new FixConverterImpl();
  }

  @Test
  @DisplayName("Teste de conversão com schema padrão")
  void testDefaultSchemaConversion() {
    String newOrderSingle = """
      8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|
        """;

    GenericRecord record = converterService.convertFixToAvro(newOrderSingle, FixVersion.FIX_4_4);

    System.out.println(record);
    assertNotNull(record, "O registro Avro não deve ser nulo.");
    assertEquals("FIX.4.4", record.get("beginString"));
    assertEquals("D", record.get("msgType"));
    // Acessa o campo 'fields' que é um mapa
    @SuppressWarnings("unchecked")
    Map<String, String> fields = (Map<String, String>) record.get("fields"); 
    // Verifica se o mapa não é nulo 
    assertNotNull(fields, "O campo 'fields' não deve ser nulo."); 
    // Acessa o valor associado à chave "11" 
    String order123 = fields.get("11"); 
    // Verifica se o valor é o esperado 
    assertEquals("Order123", order123);
  }

  @Test
  @DisplayName("Teste de conversão com schema personalizado")
  void testCustomSchemaConversion() {
    FixVersion fixVersion = FixVersion.FIX_4_4;
    fixVersion.setCustomSchemaPath(Paths.get("src/test/resources/schemas/FIX44_custom.xml"));

    String newOrderSingleCustom = """
      8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|
        """;

    GenericRecord record = converterService.convertFixToAvro(newOrderSingleCustom, fixVersion);

    assertNotNull(record, "O registro Avro não deve ser nulo.");
    assertEquals("FIX.4.4", record.get("beginString"));
    assertEquals("XX", record.get("msgType"));
    // Acessa o campo 'fields' que é um mapa 
    @SuppressWarnings("unchecked")
    Map<String, String> fields = (Map<String, String>) record.get("fields"); 
    // Verifica se o mapa não é nulo 
    assertNotNull(fields, "O campo 'fields' não deve ser nulo."); 
    // Acessa o valor associado à chave "11" 
    String order123 = fields.get("11"); 
    // Verifica se o valor é o esperado 
    assertEquals("Order123", order123);
  }

  @Test
  @DisplayName("Teste de conversão com validação e schema personalizado")
  void testCustomSchemaConversionWithValidation() {
    // Versão do FIX
    FixVersion fixVersion = FixVersion.FIX_4_4;
    fixVersion.setCustomSchemaPath(Paths.get("src/test/resources/schemas/FIX44_custom.xml"));

    // Mensagem FIX com campos válidos
    String newOrderSingleCustom = """
      8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|
        """;
    
    FixMessageParser parser = new FixMessageParser();
    
    // Critérios de validação: tag=valor esperado (String ou List<String>)
    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("8", "FIX.4.4"); // Verifica versão
    fieldCriteria.put("35", Arrays.asList("D", "G")); // MsgType deve ser "D" ou "G"
    fieldCriteria.put("54", Arrays.asList("1", "2")); // Side deve ser "1" ou "2"

    // Criando instância do validor de mensagens FIX
    FixMessageValidator validator = new FixMessageValidator();

    // Parser para obter os campos da mensagem
    Map<String, String> parsedFields = parser.parse(newOrderSingleCustom, FixVersion.FIX_4_4);

    // Validação da mensagem antes da conversão (este é o ponto chave para validar a
    // mensagem FIX)
    assertDoesNotThrow(() -> {
      // Chamando a validação da mensagem FIX
      validator.validateFields(parsedFields, FixVersion.FIX_4_4, fieldCriteria); 
    }, "A validação da mensagem FIX falhou.");

    // Se a validação passar, a conversão pode ser realizada
    GenericRecord record = converterService.convertFixToAvro(newOrderSingleCustom, fixVersion);

    // Validando o registro convertido para Avro
    assertNotNull(record, "O registro Avro não deve ser nulo.");
    assertEquals("FIX.4.4", record.get("beginString"));
    assertEquals("D", record.get("msgType"));
    // Acessa o campo 'fields' que é um mapa 
    @SuppressWarnings("unchecked")
    Map<String, String> fields = (Map<String, String>) record.get("fields"); 
    // Verifica se o mapa não é nulo 
    assertNotNull(fields, "O campo 'fields' não deve ser nulo."); 
    // Acessa o valor associado à chave "11" 
    String order123 = fields.get("11"); 
    // Verifica se o valor é o esperado 
    assertEquals("Order123", order123);
  }

  @AfterEach
  void tearDown() {
    converterService.resetToDefaultSchema(FixVersion.FIX_4_4);
  }
}
