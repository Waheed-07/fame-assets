package com.simplerishta.cms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.simplerishta.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserVerificationAttributesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserVerificationAttributesDTO.class);
        UserVerificationAttributesDTO userVerificationAttributesDTO1 = new UserVerificationAttributesDTO();
        userVerificationAttributesDTO1.setId(1L);
        UserVerificationAttributesDTO userVerificationAttributesDTO2 = new UserVerificationAttributesDTO();
        assertThat(userVerificationAttributesDTO1).isNotEqualTo(userVerificationAttributesDTO2);
        userVerificationAttributesDTO2.setId(userVerificationAttributesDTO1.getId());
        assertThat(userVerificationAttributesDTO1).isEqualTo(userVerificationAttributesDTO2);
        userVerificationAttributesDTO2.setId(2L);
        assertThat(userVerificationAttributesDTO1).isNotEqualTo(userVerificationAttributesDTO2);
        userVerificationAttributesDTO1.setId(null);
        assertThat(userVerificationAttributesDTO1).isNotEqualTo(userVerificationAttributesDTO2);
    }
}
