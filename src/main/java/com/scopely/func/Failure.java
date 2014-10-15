package com.scopely.func;

public class Failure<T> implements Try<T> {
    private final Throwable throwable;

    public Failure(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T get() {
        throw new IllegalStateException("You called get() on a failed Try", throwable);
    }

    @Override
    public Throwable getCause() {
        return throwable;
    }

    public static <T> Failure<T> of(Throwable e) {
        return new Failure<>(e);
    }
}
