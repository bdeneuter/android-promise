package org.osito.androidpromise.deferred;

public interface Converter<T, V>  {

    V transform(T value);

}
