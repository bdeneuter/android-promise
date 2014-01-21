package org.osito.androidpromise.deferred;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.osito.androidpromise.deferred.Deferred.newDeferred;

public class Function<T> {

    static boolean sync;
    private static Executable executable = new Executable(newSingleThreadExecutor());
    static boolean logStackTrace;


    public static void logOriginalStackTraceOnError(boolean enable) {
        Function.logStackTrace = enable;
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
            return executable.execute(callable);
        }
    }

    public static Executable on(Executor executor) {
        return new Executable(executor);
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
