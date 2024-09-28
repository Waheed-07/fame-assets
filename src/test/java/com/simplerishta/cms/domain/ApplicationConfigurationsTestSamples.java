package com.simplerishta.cms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationConfigurationsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApplicationConfigurations getApplicationConfigurationsSample1() {
        return new ApplicationConfigurations()
            .id(1L)
            .configKey("configKey1")
            .configValue("configValue1")
            .description("description1")
            .countryCode("countryCode1");
    }

    public static ApplicationConfigurations getApplicationConfigurationsSample2() {
        return new ApplicationConfigurations()
            .id(2L)
            .configKey("configKey2")
            .configValue("configValue2")
            .description("description2")
            .countryCode("countryCode2");
    }

    public static ApplicationConfigurations getApplicationConfigurationsRandomSampleGenerator() {
        return new ApplicationConfigurations()
            .id(longCount.incrementAndGet())
            .configKey(UUID.randomUUID().toString())
            .configValue(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .countryCode(UUID.randomUUID().toString());
    }
}
