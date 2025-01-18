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

package io.github.darioajr.converter.validation;

import io.github.darioajr.converter.core.SchemaProvider;
import java.util.List;
import java.util.Map;

/**
 * Implementation of FixMessageValidator.
 * 
 */
public class FixMessageValidator {

  /**
   * Valida os campos de uma mensagem FIX com base nos critérios fornecidos.
   *
   * @param parsedFields  Campos da mensagem FIX (chave: tag, valor: valor do campo).
   * @param schema       Versão FIX da mensagem.
   * @param fieldCriteria Critérios de validação (chave: tag, valor: String ou List of String).
   */
  public void validateFields(Map<String, String> parsedFields,
      SchemaProvider schema, Map<String, Object> fieldCriteria) {
    if (parsedFields == null || parsedFields.isEmpty()) {
      throw new IllegalArgumentException("A mensagem FIX não pode ser vazia.");
    }

    // Validações de versão
    validateVersion(parsedFields, schema);

    // Validações de campos com base nos critérios
    for (Map.Entry<String, Object> criterion : fieldCriteria.entrySet()) {
      String tag = criterion.getKey();
      Object expectedValue = criterion.getValue();

      if (!parsedFields.containsKey(tag)) {
        throw new IllegalArgumentException("O campo obrigatório está ausente: tag " + tag);
      }

      String actualValue = parsedFields.get(tag);

      // Verifica se o valor esperado é um único valor ou uma lista
      if (expectedValue instanceof String) {
        if (!expectedValue.equals(actualValue)) {
          throw new IllegalArgumentException(String.format(
            "O campo %s possui valor inválido: esperado=%s, atual=%s",
            tag, expectedValue, actualValue));
        }
      } else if (expectedValue instanceof List<?>) {
        List<?> allowedValues = (List<?>) expectedValue;
        if (!allowedValues.isEmpty() && allowedValues.get(0) instanceof String) {
          if (!allowedValues.contains(actualValue)) {
            throw new IllegalArgumentException(String.format(
       "O campo %s possui valor inválido: esperado um dos valores=%s, atual=%s",
              tag, allowedValues, actualValue));
          }
        }
      } else {
        throw new IllegalArgumentException("Critério de validação inválido para a tag " + tag);
      }
    }
  }

  /**
   * Verifica se a mensagem FIX é compatível com a versão fornecida.
   *
   * @param parsedFields Campos da mensagem FIX.
   * @param schema      Versão FIX esperada.
   */
  public void validateVersion(Map<String, String> parsedFields, SchemaProvider schema) {
    // Tag BeginString indica a versão FIX
    String beginString = parsedFields.get("8");

    if (beginString == null) {
      throw new IllegalArgumentException("A mensagem FIX não contém a tag BeginString (8).");
    }

    switch (schema.getVersion()) {
      case "44":
        if (!"FIX.4.4".equals(beginString)) {
          throw new IllegalArgumentException("Mensagem FIX incompatível com a versão FIX.4.4.");
        }
        break;
      case "50":
        if (!"FIX.5.0".equals(beginString)) {
          throw new IllegalArgumentException(
            "Mensagem FIX incompatível com a versão FIX.5.0.");
        }
        break;
      case "50SP1":
        if (!"FIX.5.0SP1".equals(beginString)) {
          throw new IllegalArgumentException("Mensagem FIX incompatível com a versão FIX.5.0SP1.");
        }
        break;
      case "50SP2":
        if (!"FIX.5.0SP2".equals(beginString)) {
          throw new IllegalArgumentException("Mensagem FIX incompatível com a versão FIX.5.0SP2.");
        }
        break;
      default:
        throw new IllegalArgumentException("Versão FIX desconhecida: " + schema);
    }
  }
}
