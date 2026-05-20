/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.infrastructure.core.jersey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.ext.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

class JerseyConfigTest {

    @Test
    void setupRegistersAnnotatedBeanTypesWithoutInstantiatingBeans() {
        ApplicationContext appCtx = mock(ApplicationContext.class);
        when(appCtx.getBeanNamesForAnnotation(Path.class)).thenReturn(new String[] { "pathBean" });
        when(appCtx.getBeanNamesForAnnotation(Provider.class)).thenReturn(new String[] { "providerBean" });
        when(appCtx.getType("pathBean")).thenReturn(TestPathResource.class);
        when(appCtx.getType("providerBean")).thenReturn(TestProvider.class);

        JerseyConfig jerseyConfig = new JerseyConfig();
        ReflectionTestUtils.setField(jerseyConfig, "appCtx", appCtx);

        jerseyConfig.setup();

        assertThat(jerseyConfig.getClasses()).contains(TestPathResource.class, TestProvider.class);
    }

    @Path("/test")
    static class TestPathResource {}

    @Provider
    static class TestProvider {}
}
