package org.osito.androidpromise.deferred;

import android.util.Log;

import java.util.concurrent.Callable;

import static java.lang.Thread.currentThread;

class DelegateRunnableWithStackTrace<V> implements Runnable {

    private Callable<V> callable;
    private Deferred<V> deferred;
    private String stackTrace;

    DelegateRunnableWithStackTrace(Callable<V> delegate, Deferred<V> deferred) {
        this.deferred = deferred;
        this.stackTrace = stackTraceToString();
    }

    public String getStackTrace() {
        return stackTrace;
    }

    private String stackTraceToString() {
        String result = "";
        for (StackTraceElement s : currentThread().getStackTrace()) {
            result = result + s.toString() + "\n\t\t";
        }
        return result;
    }


    @Override
    public void run() {
        try {
            deferred.resolve(callable.call());
        } catch(Throwable throwable) {
            Log.e("Deferred", stackTrace);
            deferred.reject(throwable);
        }
    }
}
