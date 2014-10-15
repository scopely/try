package com.scopely.func;

public class Success<T> implements Try<T> {
    private final T t;

    public Success(T t) {
        this.t = t;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T get() {
        return t;
    }

    @Override
    public Throwable getCause() {
        throw new IllegalStateException("This was a success. There was no error.");
    }
}
