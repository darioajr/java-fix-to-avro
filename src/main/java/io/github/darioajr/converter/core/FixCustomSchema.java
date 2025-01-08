/*
 * Copyright 2024 Dario Alves Junior.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.darioajr.converter.core;

import io.github.darioajr.converter.models.FixVersion;
import java.nio.file.Path;

/**
 * Implementation of Custom Fix Schema.
 * 
 */
public interface FixCustomSchema {

  /**
  * Configura um schema personalizado para uma versão específica do FIX.
  *
  * @param version Versão FIX
  * @param schemaPath Caminho do schema personalizado
  */
  void configureCustomSchema(FixVersion version, Path schemaPath);

  /**
   * Remove o schema personalizado de uma versão.
   *
   * @param version Versão FIX
   */
  void resetToDefaultSchema(FixVersion version);
}