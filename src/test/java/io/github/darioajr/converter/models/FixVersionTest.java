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
