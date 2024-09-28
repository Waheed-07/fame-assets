package com.simplerishta.cms.service.mapper;

import static com.simplerishta.cms.domain.ApplicationConfigurationsAsserts.*;
import static com.simplerishta.cms.domain.ApplicationConfigurationsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationConfigurationsMapperTest {

    private ApplicationConfigurationsMapper applicationConfigurationsMapper;

    @BeforeEach
    void setUp() {
        applicationConfigurationsMapper = new ApplicationConfigurationsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getApplicationConfigurationsSample1();
        var actual = applicationConfigurationsMapper.toEntity(applicationConfigurationsMapper.toDto(expected));
        assertApplicationConfigurationsAllPropertiesEquals(expected, actual);
    }
}
