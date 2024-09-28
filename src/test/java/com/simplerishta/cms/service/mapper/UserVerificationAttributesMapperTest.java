package com.simplerishta.cms.service.mapper;

import static com.simplerishta.cms.domain.UserVerificationAttributesAsserts.*;
import static com.simplerishta.cms.domain.UserVerificationAttributesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserVerificationAttributesMapperTest {

    private UserVerificationAttributesMapper userVerificationAttributesMapper;

    @BeforeEach
    void setUp() {
        userVerificationAttributesMapper = new UserVerificationAttributesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserVerificationAttributesSample1();
        var actual = userVerificationAttributesMapper.toEntity(userVerificationAttributesMapper.toDto(expected));
        assertUserVerificationAttributesAllPropertiesEquals(expected, actual);
    }
}
