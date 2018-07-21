package com.alekseyvalyakin.roleplaysystem.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Dagger annotation
 * Custom scope for Parent fragment
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {}
