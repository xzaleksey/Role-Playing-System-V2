package com.alekseyvalyakin.roleplaysystem.di.activity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Dagger annotation
 * Custom scope for Parent fragment
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadConfig {
    TYPE value();

    enum TYPE {
        UI,
        IO,
        COMPUTATATION
    }
}