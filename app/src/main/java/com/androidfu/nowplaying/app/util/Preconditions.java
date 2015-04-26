package com.androidfu.nowplaying.app.util;

import android.text.TextUtils;

/**
 * Created by billmote on 4/26/15.
 */
public final class Preconditions {

    private Preconditions() {
        throw new AssertionError("No instances.");
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    public static <T> T checkNotNull(T reference, String name) {
        if (reference == null) {
            throw new NullPointerException(name + " must not be null");
        }
        return reference;
    }

    public static <T extends CharSequence> T checkNotBlank(T value, String name) {
        if (TextUtils.isEmpty(value)) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}