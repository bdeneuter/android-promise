package org.osito.androidpromise.deferred;

import android.os.Handler;
import android.os.Looper;

class MainThreadTasksHolder<T> extends SameThreadTasksHolder<T> {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    @Override
    protected void notifyListener(final Task<T> task) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                MainThreadTasksHolder.super.notifyListener(task);
            }
        });
    }

    @Override
    protected void notifyErrorListener(final Task<Throwable> task) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                MainThreadTasksHolder.super.notifyErrorListener(task);
            }
        });
    }

    @Override
    protected void notifyListeners(final T data) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                MainThreadTasksHolder.super.notifyListeners(data);
            }
        });
    }

    @Override
    protected void notifyErrorListeners(final Throwable throwable) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                MainThreadTasksHolder.super.notifyErrorListeners(throwable);
            }
        });
    }
}
