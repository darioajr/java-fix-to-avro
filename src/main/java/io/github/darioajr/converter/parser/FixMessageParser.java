package io.github.darioajr.converter.parser;

import io.github.darioajr.converter.core.SchemaProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of FixMessageParser.
 * 
 */
public class FixMessageParser {

  private static final char SOH_DELIMITER = '\u0001'; // Caracter delimitador SOH
  private static final String KEY_VALUE_SEPARATOR = "=";

  /**
   * Analisa uma mensagem FIX em um mapa de pares chave-valor.
   *
   * @param fixMessage A mensagem FIX em formato String.
   * @param schema    A versão FIX usada para validações específicas (opcional).
   * @return Um mapa contendo os campos da mensagem FIX (tag, valor).
   * @throws IllegalArgumentException Se a mensagem for nula ou inválida.
   */
  public Map<String, String> parse(String fixMessage, SchemaProvider schema) {
    validateMessage(fixMessage);

    // Substitui o delimitador de barra vertical pelo delimitador SOH
    fixMessage = fixMessage.replace('|', SOH_DELIMITER);

    // Mapa para armazenar os pares de chave-valor da mensagem
    Map<String, String> parsedFields = new HashMap<>();
    // Divide a mensagem nos campos
    String[] fields = fixMessage.split(String.valueOf(SOH_DELIMITER)); 

    for (String field : fields) {
      parseField(field, parsedFields);
    }

    return parsedFields;
  }

  /**
   * Valida se a mensagem FIX é válida.
   *
   * @param fixMessage A mensagem FIX para validar.
   * @throws IllegalArgumentException Se a mensagem for nula ou vazia.
   */
  private void validateMessage(String fixMessage) {
    if (fixMessage == null || fixMessage.trim().isEmpty()) {
      throw new IllegalArgumentException("A mensagem FIX não pode ser nula ou vazia.");
    }
  }

  /**
   * Analisa um campo FIX e adiciona ao mapa de campos.
   *
   * @param field        O campo FIX em formato chave-valor (ex: "35=D").
   * @param parsedFields O mapa para armazenar os campos analisados.
   */
  private void parseField(String field, Map<String, String> parsedFields) {
    if (field == null || field.isEmpty()) {
      return; // Ignorar campos vazios
    }

    String[] keyValue = field.split(KEY_VALUE_SEPARATOR, 2); // Divide no primeiro "=" encontrado

    if (keyValue.length == 2) {
      String tag = keyValue[0].trim();
      String value = keyValue[1].trim();

      // Apenas adiciona ao mapa se ambos os valores forem válidos
      if (!tag.isEmpty() && !value.isEmpty()) {
        parsedFields.put(tag, value);
      }
    }
  }
}
