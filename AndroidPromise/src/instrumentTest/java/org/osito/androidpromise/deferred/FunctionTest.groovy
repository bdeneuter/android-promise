package org.osito.androidpromise.deferred

import org.mockito.Mock
import org.mockito.Mockito
import org.osito.androidpromise.AbstractTest
import org.osito.androidpromise.util.Assertion
import org.osito.androidpromise.util.Poller

class FunctionTest extends AbstractTest {

    @Mock
    private org.osito.androidpromise.deferred.Task<String> success

    public void testExecute() {
        org.osito.androidpromise.deferred.Promise<String> actual = new MyFunction().execute();

        actual.then(success);

        Poller.aPoller().doAssert(new Assertion() {
            @Override
            void doAssert() throws AssertionError {
                Mockito.verify(success).run(MyFunction.HELLO);
            }
        })

    }
}

