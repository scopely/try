package com.scopely.func;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The Try type represents a computation that may either result in an exception, or return a
 * successfully computed value.
 *
 * @param <T> type of the computation result
 */
public interface Try<T> {
    /**
     * @return {@code true} if the computation succeeded; in this case is safe to call
     * {@link #get()}. {@code false} if the computation threw an exception; calling {@link #get()}
     * in this state will throw an exception.
     */
    boolean isSuccess();

    /**
     * @return returns the element if {@code #isSuccess} returns true. Otherwise, throws
     * an {@link java.lang.IllegalStateException} wrapping {@link #getCause()}
     */
    T get();

    /**
     * @return returns the exception that made the computation fail
     */
    Throwable getCause();

    /**
     * Returns a Try consisting of the result of applying the given
     * function to the element of this Try.
     *
     * @param <R> The element type of the new Try
     * @param fn  a stateless function to apply to the element of this Try
     *
     * @return the new Try
     */
    default <R> Try<R> map(Function<T, R> fn) {
        if (isSuccess()) {
            try {
                return new Success<>(fn.apply(get()));
            } catch (Exception e) {
                return Failure.of(e);
            }
        } else {
            //noinspection unchecked
            return (Try<R>) this;
        }
    }

    /**
     * Returns the try returned when applying the given
     * function to the element of this try.
     *
     * @param <R> The element type of the new Try
     * @param fn  a stateless function to apply to the element of this Try
     *
     * @return the new Try
     */
    default <R> Try<R> flatMap(Function<T, Try<R>> fn) {
        if (isSuccess()) {
            try {
                return fn.apply(get());
            } catch (Exception e) {
                return Failure.of(e);
            }
        } else {
            //noinspection unchecked
            return (Try<R>) this;
        }
    }

    /**
     * Alias of {@link #map(java.util.function.Function)}
     */
    default <R> Try<R> andThen(Function<T, R> fn) {
        return map(fn);
    }

    /**
     * Returns this Try additionally performing the provided action on its element.
     *
     * @param action an action to perform on the element of this Try
     *
     * @return this Try
     */
    default Try<T> peek(Consumer<? super T> action) {
        if (isSuccess()) {
            action.accept(get());
        }
        return this;
    }

    /**
     * Creates a new try based on the computation of the specified supplier. The type
     * of the Try is the same as the return type of the supplier.
     *
     * @param supplier computation to perform
     *
     * @return a new Success if the computation succeeded. A new Failure if it threw an exception
     */
    public static <T> Try<T> tryThis(Supplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (Throwable e) {
            return Failure.of(e);
        }
    }
}
