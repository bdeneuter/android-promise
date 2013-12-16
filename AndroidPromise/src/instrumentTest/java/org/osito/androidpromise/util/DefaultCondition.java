package org.osito.androidpromise.util;

public class DefaultCondition implements Condition {

    private Runnable runnable;
    private Throwable exceptionToThrowAfterTimeout = new TimeoutException();

    public DefaultCondition(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public boolean validate() {
        try {
            runnable.run();
            return true;
        } catch (RuntimeException e) {
            exceptionToThrowAfterTimeout = e;
            return false;
        } catch (Error error) {
            exceptionToThrowAfterTimeout = error;
            return false;
        }
        catch (Throwable e) {
            exceptionToThrowAfterTimeout = new RuntimeException(e);
            return false;
        }
    }

    @Override
    public Throwable throwableToThrowAfterTimeout() {
        return exceptionToThrowAfterTimeout;
    }

}
