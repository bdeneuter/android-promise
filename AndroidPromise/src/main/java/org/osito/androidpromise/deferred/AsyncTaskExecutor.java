package org.osito.androidpromise.deferred;

import android.annotation.TargetApi;
import android.os.AsyncTask;

import java.util.concurrent.Executor;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;

public class AsyncTaskExecutor implements Executor {

    @Override
    @TargetApi(HONEYCOMB)
    public void execute(final Runnable command) {
        if (SDK_INT >= HONEYCOMB) {
            AsyncTask.execute(command);
        } else {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    command.run();
                    return null;
                }
            }.execute();
        }
    }
}
