package com.simplerishta.cms.domain;

import static com.simplerishta.cms.domain.VerifiableAttributesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.simplerishta.cms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VerifiableAttributesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VerifiableAttributes.class);
        VerifiableAttributes verifiableAttributes1 = getVerifiableAttributesSample1();
        VerifiableAttributes verifiableAttributes2 = new VerifiableAttributes();
        assertThat(verifiableAttributes1).isNotEqualTo(verifiableAttributes2);

        verifiableAttributes2.setId(verifiableAttributes1.getId());
        assertThat(verifiableAttributes1).isEqualTo(verifiableAttributes2);

        verifiableAttributes2 = getVerifiableAttributesSample2();
        assertThat(verifiableAttributes1).isNotEqualTo(verifiableAttributes2);
    }
}
