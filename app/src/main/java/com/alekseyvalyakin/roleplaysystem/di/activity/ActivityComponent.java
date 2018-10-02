package com.alekseyvalyakin.roleplaysystem.di.activity;


import com.alekseyvalyakin.roleplaysystem.app.MainActivity;
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider;
import com.alekseyvalyakin.roleplaysystem.ribs.root.RootBuilder;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent extends RootBuilder.ParentComponent, RibDependencyProvider {

    RootBuilder.Component.Builder builder();

    void inject(MainActivity mainActivity);
}
