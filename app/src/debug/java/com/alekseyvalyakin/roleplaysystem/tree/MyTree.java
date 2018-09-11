package com.alekseyvalyakin.roleplaysystem.tree;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class MyTree extends Timber.DebugTree {
    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        super.log(priority, tag, message, t);

        if (priority == Log.ERROR) {
            Crashlytics.logException(t);
        }
    }
}
