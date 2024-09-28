package com.simplerishta.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplerishta.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationConfigurationsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationConfigurationsDTO.class);
        ApplicationConfigurationsDTO applicationConfigurationsDTO1 = new ApplicationConfigurationsDTO();
        applicationConfigurationsDTO1.setId(1L);
        ApplicationConfigurationsDTO applicationConfigurationsDTO2 = new ApplicationConfigurationsDTO();
        assertThat(applicationConfigurationsDTO1).isNotEqualTo(applicationConfigurationsDTO2);
        applicationConfigurationsDTO2.setId(applicationConfigurationsDTO1.getId());
        assertThat(applicationConfigurationsDTO1).isEqualTo(applicationConfigurationsDTO2);
        applicationConfigurationsDTO2.setId(2L);
        assertThat(applicationConfigurationsDTO1).isNotEqualTo(applicationConfigurationsDTO2);
        applicationConfigurationsDTO1.setId(null);
        assertThat(applicationConfigurationsDTO1).isNotEqualTo(applicationConfigurationsDTO2);
    }
}
