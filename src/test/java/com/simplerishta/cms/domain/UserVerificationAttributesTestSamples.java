package com.simplerishta.cms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserVerificationAttributesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserVerificationAttributes getUserVerificationAttributesSample1() {
        return new UserVerificationAttributes()
            .id(1L)
            .documentType("documentType1")
            .documentUrl("documentUrl1")
            .status("status1")
            .verificationToken("verificationToken1");
    }

    public static UserVerificationAttributes getUserVerificationAttributesSample2() {
        return new UserVerificationAttributes()
            .id(2L)
            .documentType("documentType2")
            .documentUrl("documentUrl2")
            .status("status2")
            .verificationToken("verificationToken2");
    }

    public static UserVerificationAttributes getUserVerificationAttributesRandomSampleGenerator() {
        return new UserVerificationAttributes()
            .id(longCount.incrementAndGet())
            .documentType(UUID.randomUUID().toString())
            .documentUrl(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .verificationToken(UUID.randomUUID().toString());
    }
}
