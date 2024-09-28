package com.simplerishta.cms.service.mapper;

import static com.simplerishta.cms.domain.VerifiableAttributesAsserts.*;
import static com.simplerishta.cms.domain.VerifiableAttributesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VerifiableAttributesMapperTest {

    private VerifiableAttributesMapper verifiableAttributesMapper;

    @BeforeEach
    void setUp() {
        verifiableAttributesMapper = new VerifiableAttributesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVerifiableAttributesSample1();
        var actual = verifiableAttributesMapper.toEntity(verifiableAttributesMapper.toDto(expected));
        assertVerifiableAttributesAllPropertiesEquals(expected, actual);
    }
}
