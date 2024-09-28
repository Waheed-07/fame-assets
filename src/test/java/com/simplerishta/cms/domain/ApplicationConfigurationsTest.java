package com.simplerishta.cms.domain;

import static com.simplerishta.cms.domain.ApplicationConfigurationsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.simplerishta.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationConfigurationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationConfigurations.class);
        ApplicationConfigurations applicationConfigurations1 = getApplicationConfigurationsSample1();
        ApplicationConfigurations applicationConfigurations2 = new ApplicationConfigurations();
        assertThat(applicationConfigurations1).isNotEqualTo(applicationConfigurations2);

        applicationConfigurations2.setId(applicationConfigurations1.getId());
        assertThat(applicationConfigurations1).isEqualTo(applicationConfigurations2);

        applicationConfigurations2 = getApplicationConfigurationsSample2();
        assertThat(applicationConfigurations1).isNotEqualTo(applicationConfigurations2);
    }
}
