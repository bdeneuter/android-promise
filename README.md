android-promise
===============

A simple library for promise/deferred in Android. Instead of using an AsyncTask, you can return a deferred to the caller while you start work on a background thread. When the work is finished you can resolve or reject the deferred.
The users of the promise object can add tasks that need to be executed when the deferred is resolved or rejected. The user can specify on which thread this task needs to be executed.

The user of the promise doesn't need to worry if a promise is already resolved or not. If it is already resolved, the task will be executed immediatly. If not, the task will be scheduled to be executed when the deferred is resolved.

See also http://bdeneuter.github.io/android-promise/
