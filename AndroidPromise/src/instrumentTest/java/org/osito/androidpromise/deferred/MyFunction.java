package org.osito.androidpromise.deferred;

public class MyFunction extends Function<String> {

    public static final String HELLO = "Hello";

    @Override
    protected String run() {
        return HELLO;
    }
}

