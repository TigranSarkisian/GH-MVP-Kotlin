package com.sarkisian.gh.data.entity;

import android.support.annotation.Nullable;

public class Optional<M> {

    private final M optional;

    public Optional(@Nullable M optional) {
        this.optional = optional;
    }

    public boolean isEmpty() {
        return this.optional == null;
    }

    public M get() {
        return optional;
    }

}
