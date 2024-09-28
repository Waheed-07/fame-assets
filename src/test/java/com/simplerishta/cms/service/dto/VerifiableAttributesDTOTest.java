package com.simplerishta.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplerishta.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VerifiableAttributesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VerifiableAttributesDTO.class);
        VerifiableAttributesDTO verifiableAttributesDTO1 = new VerifiableAttributesDTO();
        verifiableAttributesDTO1.setId(1L);
        VerifiableAttributesDTO verifiableAttributesDTO2 = new VerifiableAttributesDTO();
        assertThat(verifiableAttributesDTO1).isNotEqualTo(verifiableAttributesDTO2);
        verifiableAttributesDTO2.setId(verifiableAttributesDTO1.getId());
        assertThat(verifiableAttributesDTO1).isEqualTo(verifiableAttributesDTO2);
        verifiableAttributesDTO2.setId(2L);
        assertThat(verifiableAttributesDTO1).isNotEqualTo(verifiableAttributesDTO2);
        verifiableAttributesDTO1.setId(null);
        assertThat(verifiableAttributesDTO1).isNotEqualTo(verifiableAttributesDTO2);
    }
}
