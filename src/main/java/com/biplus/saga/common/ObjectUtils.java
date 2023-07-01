package com.biplus.saga.common;

import java.util.function.Supplier;

public class ObjectUtils {

    public static <T> T opt(Supplier<T> statement) {
        try {
            return statement.get();
        } catch (NullPointerException exc) {
            return null;
        }
    }
}
