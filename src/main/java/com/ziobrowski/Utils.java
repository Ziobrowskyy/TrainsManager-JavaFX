package com.ziobrowski;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    static final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public static int getInt() {
        return threadLocalRandom.nextInt();
    }

    public static int getInt(int max) {
        return threadLocalRandom.nextInt(max);
    }

    public static int getInt(int min, int max) {
        return threadLocalRandom.nextInt(min, max);
    }

    public static double getDouble(double min, double max) {
        return threadLocalRandom.nextDouble(min, max);
    }

}
