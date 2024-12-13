package io.github.darioajr.converter.models;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import static org.assertj.core.api.Assertions.*;

class FixVersionTest {
    @Test
    void testDefaultSchemaPath() {
        assertThat(FixVersion.FIX_4_4.getSchemaPath())
                .isEqualTo("src/test/resources/schemas/FIX44_custom.xml");
    }

    @Test
    void testCustomSchemaConfiguration() {
        FixVersion version = FixVersion.FIX_4_4;

        // Verifica estado inicial
        assertThat(version.hasCustomSchema()).isFalse();

        // Configura schema customizado
        version.setCustomSchemaPath(Paths.get("src/test/resources/schemas/FIX44_custom.xml"));

        // Verifica mudança
        assertThat(version.hasCustomSchema()).isTrue();
        assertThat(version.getSchemaPath())
                .isEqualTo("src/test/resources/schemas/FIX44_custom.xml");
    }

    @Test
    void testResetToDefaultSchema() {
        FixVersion version = FixVersion.FIX_4_4;
        version.setCustomSchemaPath(Paths.get("src/test/resources/schemas/FIX44_custom.xml"));

        // Reseta para schema padrão
        version.setCustomSchemaPath(null);

        assertThat(version.hasCustomSchema()).isFalse();
        assertThat(version.getSchemaPath())
                .isEqualTo("src/main/resources/schemas/FIX44.xml");
    }
}
