package org.osito.androidpromise.deferred;


import org.mockito.Mock;
import org.osito.androidpromise.AbstractTest;
import org.osito.androidpromise.deferred.BooleanDeferred;
import org.osito.androidpromise.util.Assertion;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.osito.androidpromise.deferred.BooleanDeferred.newDeferred;
import static org.osito.androidpromise.util.Poller.aPoller;

public class BooleanDeferredTest extends AbstractTest {

    @Mock
    private Runnable runnable, otherRunnable;

    private BooleanDeferred deferred = newDeferred();

    public void testIfTrueThen()  {
        // GIVEN
        deferred.ifTrueThen(runnable)
                .ifFalseThen(otherRunnable);

        // WHEN
        deferred.resolve(true);

        // THEN
        verify(runnable).run();
        verifyZeroInteractions(otherRunnable);
    }

    public void testIfFalseThen()  {
        // GIVEN
        deferred.ifTrueThen(runnable)
                .ifFalseThen(otherRunnable);

        // WHEN
        deferred.resolve(false);

        // THEN
        verify(otherRunnable).run();
        verifyZeroInteractions(runnable);
    }

    public void testIfTrueThenOnMainThread()  {
        // GIVEN
        deferred.ifTrueThenOnMainThread(runnable)
                .ifFalseThenOnMainThread(otherRunnable);

        // WHEN
        deferred.resolve(true);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                verify(runnable).run();
                verifyZeroInteractions(otherRunnable);
            }
        });
    }

    public void testIfFalseThenOnMainThread()  {
        // GIVEN
        deferred.ifTrueThenOnMainThread(runnable)
                .ifFalseThenOnMainThread(otherRunnable);

        // WHEN
        deferred.resolve(false);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                verify(otherRunnable).run();
                verifyZeroInteractions(runnable);
            }
        });

    }

    public void testIfTrueThenOnBackgroundThread()  {
        // GIVEN
        deferred.ifTrueThenOnBackgroundThread(runnable)
                .ifFalseThenOnBackgroundThread(otherRunnable);

        // WHEN
        deferred.resolve(true);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                verify(runnable).run();
                verifyZeroInteractions(otherRunnable);
            }
        });
    }

    public void testIfFalseThenOnBackgroundThread()  {
        // GIVEN
        deferred.ifTrueThenOnBackgroundThread(runnable)
                .ifFalseThenOnBackgroundThread(otherRunnable);

        // WHEN
        deferred.resolve(false);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                verify(otherRunnable).run();
                verifyZeroInteractions(runnable);
            }
        });
    }

    public void testIfTrueThenAsync()  {
        // GIVEN
        deferred.ifTrueThenAsync(runnable)
                .ifFalseThenAsync(otherRunnable);

        // WHEN
        deferred.resolve(true);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                verify(runnable).run();
                verifyZeroInteractions(otherRunnable);
            }
        });

    }

    public void testIfFalseThenAsync()  {
        // GIVEN
        deferred.ifTrueThenAsync(runnable)
                .ifFalseThenAsync(otherRunnable);

        // WHEN
        deferred.resolve(false);

        // THEN
        aPoller().doAssert(new Assertion() {
            @Override
            public void doAssert() throws AssertionError {
                verify(otherRunnable).run();
                verifyZeroInteractions(runnable);
            }
        });

    }
}
