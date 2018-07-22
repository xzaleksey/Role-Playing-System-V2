package com.alekseyvalyakin.roleplaysystem.di.singleton;

import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityComponent;
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Base app component
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent extends SingletonDependencyProvider {
    ActivityComponent activityComponent(ActivityModule activityModule);
}
