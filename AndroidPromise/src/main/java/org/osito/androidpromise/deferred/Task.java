package org.osito.androidpromise.deferred;

public interface Task<T> {

    void run(T data);

}
