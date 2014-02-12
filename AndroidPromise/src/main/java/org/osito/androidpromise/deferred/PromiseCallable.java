package org.osito.androidpromise.deferred;

public interface PromiseCallable<T> {

    Promise<T> call();

}
