package org.osito.androidpromise.deferred;

import android.os.AsyncTask;

import static org.osito.androidpromise.deferred.Deferred.newDeferred;

public abstract class Function<T> {

    public final Promise<T> execute() {
        final Deferred<T> deferred = newDeferred();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    deferred.resolve(run());
                } catch(Throwable throwable) {
                    deferred.reject(throwable);
                }
                return null;
            }
        }.execute();

        return deferred;
    }

    protected abstract T run();

}
