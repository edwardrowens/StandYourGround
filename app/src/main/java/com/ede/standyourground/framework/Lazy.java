package com.ede.standyourground.framework;

/**
 *
 */

public class Lazy {
    public static <T> dagger.Lazy<T> of(final T t) {
        return new dagger.Lazy<T>() {
            @Override
            public T get() {
                return t;
            }
        };
    }
}
