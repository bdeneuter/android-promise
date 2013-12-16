package org.osito.androidpromise.util;

import static org.osito.androidpromise.util.Sleeper.sleep;

public class Poller {

    private static final int POLLING_INTERVAL_MS = 100;
    private static final int TIMEOUT_MS = 5000;

    private int pollingInterval = POLLING_INTERVAL_MS;
    private int timeout = TIMEOUT_MS;
    private Condition condition;

    private Poller() {
    }

    public static Poller aPoller() {
        return new Poller();
    }

    public void doAssert(final Assertion assertion) {
        withCondition(new DefaultCondition(new Runnable() {

            @Override
            public void run() {
                try {
                    assertion.doAssert();
                } catch (AssertionError e) {
                    throw e;
                }
            }
        })).poll();
    }

    public void doRun(Runnable runnable) {
        withCondition(new DefaultCondition(runnable)).poll();
    }

    public Poller withCondition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public Poller withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public Poller withPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
        return this;
    }

    public void poll() {
        long startTime = System.currentTimeMillis();
        while (!condition.validate() && getDurationSince(startTime) < timeout) {
            sleep(pollingInterval);
        }
        if (getDurationSince(startTime) >= timeout) {
            throwException(condition.throwableToThrowAfterTimeout());
        }
    }

    private void throwException(Throwable throwable) {
        if (throwable instanceof Error) {
            throw (Error)throwable;
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        throw new RuntimeException(throwable);
    }

    private long getDurationSince(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

}
