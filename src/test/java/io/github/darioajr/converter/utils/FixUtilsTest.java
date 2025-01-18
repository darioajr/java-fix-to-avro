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

package io.github.darioajr.converter.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import org.junit.jupiter.api.Test;
import quickfix.Message;

/**
 * Implementation of FixUtilsTest.
 * 
 */
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
