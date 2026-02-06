package com.airfrance.admin.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OffreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Offre getOffreSample1() {
        return new Offre().id(1L);
    }

    public static Offre getOffreSample2() {
        return new Offre().id(2L);
    }

    public static Offre getOffreRandomSampleGenerator() {
        return new Offre().id(longCount.incrementAndGet());
    }
}
