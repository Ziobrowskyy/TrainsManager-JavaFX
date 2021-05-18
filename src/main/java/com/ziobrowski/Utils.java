package com.ziobrowski;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    static final ThreadLocalRandom rand = ThreadLocalRandom.current();

    public static int getRandInt(int max) {
        return rand.nextInt(max);
    }

    public static int getRandInt(int min, int max) {
        return rand.nextInt(min, max);
    }
}
