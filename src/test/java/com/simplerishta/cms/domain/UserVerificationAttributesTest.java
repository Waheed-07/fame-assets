package com.simplerishta.cms.domain;

import static com.simplerishta.cms.domain.UserVerificationAttributesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.simplerishta.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserVerificationAttributesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserVerificationAttributes.class);
        UserVerificationAttributes userVerificationAttributes1 = getUserVerificationAttributesSample1();
        UserVerificationAttributes userVerificationAttributes2 = new UserVerificationAttributes();
        assertThat(userVerificationAttributes1).isNotEqualTo(userVerificationAttributes2);

        userVerificationAttributes2.setId(userVerificationAttributes1.getId());
        assertThat(userVerificationAttributes1).isEqualTo(userVerificationAttributes2);

        userVerificationAttributes2 = getUserVerificationAttributesSample2();
        assertThat(userVerificationAttributes1).isNotEqualTo(userVerificationAttributes2);
    }
}
