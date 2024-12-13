package io.github.darioajr.converter.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import quickfix.Message;

public class FixUtilsTest {

    @Test
    public void testGetFieldsAsMap() throws Exception {
        // Criação da mensagem de teste
        Message message = new Message();
        
        message.setString(8, "FIX.4.4"); // BeginString
        message.setString(35, "D"); // MsgType
        message.setString(55, "AAPL"); // Symbol

        // Chamada do método a ser testado
        Map<String, String> fieldsMap = FixUtils.getFieldsAsMap(message);
        System.out.println(fieldsMap);

        // Verificações
        assertNotNull(fieldsMap);
        assertEquals("FIX.4.4", fieldsMap.get("8"));
        assertEquals("D", fieldsMap.get("35"));
        assertEquals("AAPL", fieldsMap.get("55"));
    }
}
