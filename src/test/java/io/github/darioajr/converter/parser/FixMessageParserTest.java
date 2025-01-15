package io.github.darioajr.converter.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.darioajr.converter.models.FixDefaultVersion;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FixMessageParserTest {

  private final FixMessageParser parser = new FixMessageParser();

  @Test
  void shouldParseValidFixMessage() {
    String fixMessage = """
      8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|
        """;

    Map<String, String> parsedFields = parser.parse(fixMessage, FixDefaultVersion.FIX_4_4);

    assertThat(parsedFields)
        .isNotEmpty()
        .containsEntry("8", "FIX.4.4")
        .containsEntry("35", "D")
        .containsEntry("55", "AAPL");
  }

  @Test
  void shouldThrowExceptionForNullMessage() {
    assertThatThrownBy(() -> parser.parse(null, FixDefaultVersion.FIX_4_4))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("A mensagem FIX não pode ser nula ou vazia.");
  }

  @Test
  void shouldThrowExceptionForEmptyMessage() {
    assertThatThrownBy(() -> parser.parse("", FixDefaultVersion.FIX_4_4))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("A mensagem FIX não pode ser nula ou vazia.");
  }

  @Test
  void shouldIgnoreEmptyFields() {
    String fixMessage = "8=FIX.4.4|9=123|35=D|||49=SenderCompID|";

    Map<String, String> parsedFields = parser.parse(fixMessage, FixDefaultVersion.FIX_4_4);

    assertThat(parsedFields)
        .isNotEmpty()
        .containsEntry("8", "FIX.4.4")
        .doesNotContainKey(""); // Campo vazio deve ser ignorado
  }
}
