package org.osito.androidpromise.deferred;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import static org.osito.androidpromise.deferred.Deferred.newDeferred;

public class Executable {

    private Executor executor;

    public Executable(Executor executor) {
        this.executor = executor;
    }

    public <V> Promise<V> execute(final PromiseCallable<V> callable) {
        final Deferred<V> deferred = newDeferred();
        Runnable runnable = new DelegatePromiseRunnable<V>(callable, deferred);
        if (Function.sync) {
            runnable.run();
        } else {
            executor.execute(runnable);
        }
        return deferred;
    }

    public <V> Promise<V> execute(Callable<V> callable) {
        final Deferred<V> deferred = newDeferred();
        Runnable runnable = new DelegateRunnable<V>(callable, deferred);
        if (Function.sync) {
            runnable.run();
        } else {
            executor.execute(runnable);
        }
        return deferred;
    }
}
