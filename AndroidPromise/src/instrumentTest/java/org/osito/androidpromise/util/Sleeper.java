package org.osito.androidpromise.util;

public class Sleeper {

    public static final int DEFAULT_SLEEP_TIME = 5000;

    static void sleep() {
        sleep(DEFAULT_SLEEP_TIME);
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
