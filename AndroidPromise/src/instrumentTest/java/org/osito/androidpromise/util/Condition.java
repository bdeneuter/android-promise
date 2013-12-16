package org.osito.androidpromise.util;

public interface Condition {

    boolean validate();

    Throwable throwableToThrowAfterTimeout();

}
