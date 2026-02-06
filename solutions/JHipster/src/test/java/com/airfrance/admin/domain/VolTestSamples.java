package com.airfrance.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VolTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vol getVolSample1() {
        return new Vol().id(1L).origin("origin1").destination("destination1");
    }

    public static Vol getVolSample2() {
        return new Vol().id(2L).origin("origin2").destination("destination2");
    }

    public static Vol getVolRandomSampleGenerator() {
        return new Vol().id(longCount.incrementAndGet()).origin(UUID.randomUUID().toString()).destination(UUID.randomUUID().toString());
    }
}
