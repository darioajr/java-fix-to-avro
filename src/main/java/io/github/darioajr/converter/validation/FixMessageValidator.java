package io.github.darioajr.converter.validation;

import io.github.darioajr.converter.models.FixVersion;

import java.util.List;
import java.util.Map;

public class FixMessageValidator {

    /**
     * Valida os campos de uma mensagem FIX com base nos critérios fornecidos.
     *
     * @param parsedFields  Campos da mensagem FIX (chave: tag, valor: valor do campo).
     * @param version       Versão FIX da mensagem.
     * @param fieldCriteria Critérios de validação (chave: tag, valor: String ou List<String>).
     */
    public void validateFields(Map<String, String> parsedFields, FixVersion version, Map<String, Object> fieldCriteria) {
        if (parsedFields == null || parsedFields.isEmpty()) {
            throw new IllegalArgumentException("A mensagem FIX não pode ser vazia.");
        }

        // Validações de versão
        validateVersion(parsedFields, version);

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
                    throw new IllegalArgumentException(String.format("O campo %s possui valor inválido: esperado=%s, atual=%s",
                            tag, expectedValue, actualValue));
                }
            } else if (expectedValue instanceof List<?>) {
                List<?> allowedValues = (List<?>) expectedValue;
                if (!allowedValues.isEmpty() && allowedValues.get(0) instanceof String) {
                    if (!allowedValues.contains(actualValue)) {
                        throw new IllegalArgumentException(String.format("O campo %s possui valor inválido: esperado um dos valores=%s, atual=%s",
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
     * @param version      Versão FIX esperada.
     */
    public void validateVersion(Map<String, String> parsedFields, FixVersion version) {
        String beginString = parsedFields.get("8"); // Tag BeginString indica a versão FIX

        if (beginString == null) {
            throw new IllegalArgumentException("A mensagem FIX não contém a tag BeginString (8).");
        }

        switch (version) {
            case FIX_4_4:
                if (!"FIX.4.4".equals(beginString)) {
                    throw new IllegalArgumentException("Mensagem FIX incompatível com a versão FIX.4.4.");
                }
                break;
            case FIX_5_0:
                if (!"FIX.5.0".equals(beginString)) {
                    throw new IllegalArgumentException("Mensagem FIX incompatível com a versão FIX.5.0.");
                }
                break;
            case FIX_5_0_SP1:
                if (!"FIX.5.0SP1".equals(beginString)) {
                    throw new IllegalArgumentException("Mensagem FIX incompatível com a versão FIX.5.0SP1.");
                }
                break;
            case FIX_5_0_SP2:
                if (!"FIX.5.0SP2".equals(beginString)) {
                    throw new IllegalArgumentException("Mensagem FIX incompatível com a versão FIX.5.0SP2.");
                }
                break;
            default:
                throw new IllegalArgumentException("Versão FIX desconhecida: " + version);
        }
    }
}
