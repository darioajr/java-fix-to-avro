package io.github.darioajr.converter.core;

import io.github.darioajr.converter.models.FixVersion;
import java.nio.file.Path;

public interface FixCustomSchema {
    /**
     * Configura um schema personalizado para uma versão específica do FIX
     * @param version Versão FIX
     * @param schemaPath Caminho do schema personalizado
     */
    void configureCustomSchema(FixVersion version, Path schemaPath);

    /**
     * Remove o schema personalizado de uma versão
     * @param version Versão FIX
     */
    void resetToDefaultSchema(FixVersion version);
}