package com.alekseyvalyakin.roleplaysystem.di;


import com.alekseyvalyakin.roleplaysystem.ribs.root.RootBuilder;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent extends RootBuilder.ParentComponent {

    RootBuilder.Component.Builder builder();
}
