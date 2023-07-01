package com.biplus.saga.domain.type;

public interface IType<V> {
    String key();

    V value();

    String label();
}