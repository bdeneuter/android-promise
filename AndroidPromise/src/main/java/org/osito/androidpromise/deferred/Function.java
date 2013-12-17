package org.osito.androidpromise.deferred;

import android.os.AsyncTask;

import java.util.concurrent.Callable;

import static org.osito.androidpromise.deferred.Deferred.newDeferred;

public class Function<T> {

    public static <T> Promise<T> execute(final Callable<T> callable) {
        final Deferred<T> deferred = newDeferred();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    deferred.resolve(callable.call());
                } catch(Throwable throwable) {
                    deferred.reject(throwable);
                }
                return null;
            }
        }.execute();

        return deferred;
    }

}
