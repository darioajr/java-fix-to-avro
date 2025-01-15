package io.github.darioajr.converter.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FixVersionTest {
  @Test
  void testDefaultSchemaPath() {
    FixDefaultVersion version = FixDefaultVersion.FIX_4_4;
    assertThat(version.getSchemaPath())
        .isEqualTo(getClass().getClassLoader().getResource("schemas/FIX44.xml").getPath());
  }

  @Test
  void testCustomSchemaConfiguration() {
    FixCustomVersion customVersion = new FixCustomVersion(FixDefaultVersion.FIX_4_4.getVersion(),
        getClass().getClassLoader().getResource("schemas/FIX44_custom.xml").getPath());

    assertThat(customVersion.getVersion()).isEqualTo(FixDefaultVersion.FIX_4_4.getVersion());
    assertThat(customVersion.getSchemaPath())
        .isEqualTo(getClass().getClassLoader().getResource("schemas/FIX44_custom.xml").getPath());
  }

}
