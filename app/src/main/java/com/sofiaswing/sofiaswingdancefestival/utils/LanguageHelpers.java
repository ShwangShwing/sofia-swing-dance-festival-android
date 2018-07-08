package com.sofiaswing.sofiaswingdancefestival.utils;

import android.support.annotation.NonNull;

public class LanguageHelpers {
    public static <T> String toStringWithDefault(T value, @NonNull String defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        return value.toString();
    }
}
