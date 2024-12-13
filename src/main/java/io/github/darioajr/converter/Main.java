package io.github.darioajr.converter;

import io.github.darioajr.converter.core.FixConverterImpl;
import io.github.darioajr.converter.models.FixVersion;
import io.github.darioajr.converter.parser.FixMessageParser;
import io.github.darioajr.converter.validation.FixMessageValidator;

import org.apache.avro.generic.GenericRecord;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Usa schema padrão
        FixConverterImpl converterService = new FixConverterImpl();
        String newOrderSingle = "8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|";
        GenericRecord defaultRecord = converterService.convertFixToAvro(newOrderSingle, FixVersion.FIX_4_4);
        System.out.println(defaultRecord);

        // Usa schema personalizado
        FixVersion fixVersion = FixVersion.FIX_4_4;
        fixVersion.setCustomSchemaPath(Paths.get("src/main/resources/schemas/FIX44_custom.xml"));
        String newOrderSingleCustom = "8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|";
        GenericRecord customRecord = converterService.convertFixToAvro(newOrderSingleCustom, fixVersion);
        System.out.println(customRecord);

        converterService.resetToDefaultSchema(FixVersion.FIX_4_4);

        FixMessageParser parser = new FixMessageParser();
        FixMessageValidator validator = new FixMessageValidator();

        // Mensagem FIX de exemplo
        String fixMessage = "8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|";

        // Parser para obter os campos da mensagem
        Map<String, String> parsedFields = parser.parse(fixMessage, FixVersion.FIX_4_4);

        // Critérios de validação: tag=valor esperado (String ou List<String>)
        Map<String, Object> fieldCriteria = new HashMap<>();
        fieldCriteria.put("8", "FIX.4.4"); // Verifica versão
        fieldCriteria.put("35", Arrays.asList("D", "G")); // MsgType deve ser "D" ou "G"
        fieldCriteria.put("54", Arrays.asList("1", "2")); // Side deve ser "1" ou "2"

        // Validação
        validator.validateFields(parsedFields, FixVersion.FIX_4_4, fieldCriteria);

        System.out.println("Mensagem válida!");
    }
}