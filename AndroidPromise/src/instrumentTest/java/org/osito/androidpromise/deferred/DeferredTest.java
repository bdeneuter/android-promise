package org.osito.androidpromise.deferred;


import android.test.UiThreadTest;

import org.mockito.InOrder;
import org.mockito.Mock;
import org.osito.androidpromise.AbstractTest;
import org.osito.androidpromise.util.Assertion;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.osito.androidpromise.deferred.Deferred.newDeferred;
import static org.osito.androidpromise.util.Poller.aPoller;

public class DeferredTest extends AbstractTest {

    public static final String DATA = "a result";

    @Mock
    private Task task, otherTask,
            rejectTask, otherRejectTask;

    private Deferred<Object> deferred = newDeferred();

    public void testResolve_ThenExecuteTheRegisteredCommandsInOrder() {
        // GIVEN
        deferred.then(task)
                .then(otherTask)
                .onError(rejectTask);

        // WHEN
        deferred.resolve(DATA);

        // THEN
        InOrder inOrder = inOrder(task, otherTask);
        inOrder.verify(task).run(DATA);
        inOrder.verify(otherTask).run(DATA);

        verifyZeroInteractions(rejectTask);
    }

    public void testResolve_WhenThenOnMainThread_ThenExecuteTheRegisteredCommandsInOrder() {
        // GIVEN
        deferred.thenOnMainThread(task)
                .thenOnMainThread(otherTask);

        // WHEN
        deferred.resolve(DATA);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                InOrder inOrder = inOrder(task, otherTask);
                inOrder.verify(task).run(DATA);
                inOrder.verify(otherTask).run(DATA);

                verifyZeroInteractions(rejectTask);
            }
        });
    }

    public void testResolve_WhenThenOnBackground_ThenExecuteTheRegisteredCommandsInOrder() {
        // GIVEN
        deferred.thenOnBackgroundThread(task)
                .thenOnBackgroundThread(otherTask);

        // WHEN
        deferred.resolve(DATA);

        // THEN
        InOrder inOrder = inOrder(task, otherTask);
        inOrder.verify(task).run(DATA);
        inOrder.verify(otherTask).run(DATA);

        verifyZeroInteractions(rejectTask);
    }

    public void testResolve_WhenThenAsync_ThenExecuteTheRegisteredCommandsInOrder() {
        // GIVEN
        deferred.thenAsync(task)
                .thenAsync(otherTask);

        // WHEN
        deferred.resolve(DATA);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                verify(task).run(DATA);
                verify(otherTask).run(DATA);

                verifyZeroInteractions(rejectTask);
            }
        });
    }

    public void testReject_ThenExecuteRejectCommandsInOrder() {
        // GIVEN
        RuntimeException exception = new RuntimeException();
        deferred.onError(rejectTask)
                .onError(otherRejectTask)
                .then(task);

        // WHEN
        deferred.reject(exception);

        // THEN
        InOrder inOrder = inOrder(rejectTask, otherRejectTask);
        inOrder.verify(rejectTask).run(exception);
        inOrder.verify(otherRejectTask).run(exception);
        verifyZeroInteractions(task);
    }

    public void testReject_WhenOnErrorOnMainThread_ThenExecuteRejectCommandsInOrder() {
        // GIVEN
        final RuntimeException exception = new RuntimeException();
        deferred.onErrorOnMainThread(rejectTask)
                .onErrorOnMainThread(otherRejectTask)
                .thenOnMainThread(task);

        // WHEN
        deferred.reject(exception);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                InOrder inOrder = inOrder(rejectTask, otherRejectTask);
                inOrder.verify(rejectTask).run(exception);
                inOrder.verify(otherRejectTask).run(exception);
                verifyZeroInteractions(task);
            }
        });
    }

    public void testReject_WhenOnErrorOnBackgroundThread_ThenExecuteRejectCommandsInOrder() {
        // GIVEN
        final RuntimeException exception = new RuntimeException();
        deferred.onErrorOnBackgroundThread(rejectTask)
                .onErrorOnBackgroundThread(otherRejectTask)
                .thenOnBackgroundThread(task);

        // WHEN
        deferred.reject(exception);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                InOrder inOrder = inOrder(rejectTask, otherRejectTask);
                inOrder.verify(rejectTask).run(exception);
                inOrder.verify(otherRejectTask).run(exception);
                verifyZeroInteractions(task);
            }
        });

    }

    @UiThreadTest
    public void testReject_WhenOnErrorOnBackgroundThread_OnMainThread_ThenExecuteRejectCommandsInOrderInBackground() {
        // GIVEN
        final RuntimeException exception = new RuntimeException();
        deferred.onErrorOnBackgroundThread(rejectTask)
                .onErrorOnBackgroundThread(otherRejectTask)
                .thenOnBackgroundThread(task);

        // WHEN
        deferred.reject(exception);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                InOrder inOrder = inOrder(rejectTask, otherRejectTask);
                inOrder.verify(rejectTask).run(exception);
                inOrder.verify(otherRejectTask).run(exception);
                verifyZeroInteractions(task);
            }
        });

    }

    public void testReject_WhenOnErrorAsync_ThenExecuteRejectCommandsInOrder() {
        // GIVEN
        final RuntimeException exception = new RuntimeException();
        deferred.onErrorAsync(rejectTask)
                .onErrorAsync(otherRejectTask)
                .thenOnBackgroundThread(task);

        // WHEN
        deferred.reject(exception);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                verify(rejectTask).run(exception);
                verify(otherRejectTask).run(exception);
                verifyZeroInteractions(task);
            }
        });

    }

    public void testResolve_WhenDataAlreadyResolved_ThenExecuteTheRegisteredCommandsInOrder() {
        // GIVEN
        deferred.resolve(DATA);

        // WHEN
        deferred.then(task)
                .then(otherTask);

        // THEN
        InOrder inOrder = inOrder(task, otherTask);
        inOrder.verify(task).run(DATA);
        inOrder.verify(otherTask).run(DATA);
    }

    public void testResolve_WhenAlreadyResolved_ThenThrowException() {
        // GIVEN
        deferred.resolve(DATA);

        // WHEN
        try {
            deferred.resolve(DATA);
            fail();
        } catch (IllegalStateException ex) {

        }
    }

    public void testResolve_WhenAlreadyRejected_ThenThrowException() {
        // GIVEN
        deferred.reject();

        // WHEN
        try {
            deferred.resolve(DATA);
            fail();
        } catch (IllegalStateException ex) {

        }
    }

    public void testReject_WhenAlreadyResolved_ThenThrowException() {
        // GIVEN
        RuntimeException exception = new RuntimeException();
        deferred.resolve(DATA);

        // WHEN
        try {
            deferred.reject(exception);
            fail();
        } catch (IllegalStateException ex) {

        }
    }

    public void testReject_WhenAlreadyRejected_ThenThrowException() {
        // GIVEN
        RuntimeException exception = new RuntimeException();
        deferred.reject(exception);

        // WHEN
        try {
            deferred.reject(exception);
            fail();
        } catch (IllegalStateException ex) {

        }
    }

}
