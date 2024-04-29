package com.foodtec.pingpong.util;

/**
 * A simple pair class to hold two values.
 * @param first
 * @param second
 */
public record Pair<F, S>(F first, S second) {
    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }
}
