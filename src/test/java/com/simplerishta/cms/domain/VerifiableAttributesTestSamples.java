package com.simplerishta.cms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VerifiableAttributesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VerifiableAttributes getVerifiableAttributesSample1() {
        return new VerifiableAttributes().id(1L).name("name1").createdBy("createdBy1");
    }

    public static VerifiableAttributes getVerifiableAttributesSample2() {
        return new VerifiableAttributes().id(2L).name("name2").createdBy("createdBy2");
    }

    public static VerifiableAttributes getVerifiableAttributesRandomSampleGenerator() {
        return new VerifiableAttributes()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
