package com.alekseyvalyakin.roleplaysystem.di;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Base app component
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    ActivityComponent activityComponent(ActivityModule activityModule);
}
