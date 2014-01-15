package org.osito.androidpromise.deferred;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.osito.androidpromise.deferred.Deferred.newDeferred;

public class Function<T> {

    private static boolean sync;
    private static Executor executor = newSingleThreadExecutor();
    private static boolean logStackTrace;

    public static void logOriginalStackTraceOnError(boolean enable) {
        Function.logStackTrace = enable;
    }

    public static void setExecutor(Executor executor) {
        Function.executor = executor;
    }

    public static void enableAlwaysSyncCalls() {
        sync = true;
    }

    public static void disableAlwaysSyncCalls() {
        sync = false;
    }

    public static <T> Promise<T> execute(final Callable<T> callable) {
        if (sync) {
            return executeSync(callable);
        } else {
            final Deferred<T> deferred = newDeferred();
            if (logStackTrace) {
                executor.execute(new DelegateRunnableWithStackTrace<T>(callable, deferred));
            } else {
                executor.execute(new DelegateRunnable<T>(callable, deferred));
            }
            return deferred;
        }


    }

    public static <T> Promise<T> executeSync(Callable<T> callable) {
        Deferred<T> deferred = newDeferred();
        try {
            deferred.resolve(callable.call());
        } catch(Throwable throwable) {
            deferred.reject(throwable);
        }
        return deferred;
    }

}
