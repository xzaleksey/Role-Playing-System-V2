package com.alekseyvalyakin.roleplaysystem.data.prefs;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class SharedPreferencesHelper implements LocalKeyValueStorage {
    private static final String LOGIN = "login";
    private SharedPreferences preferences;

    public SharedPreferencesHelper(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void setLogin(@NonNull String login) {
        preferences
                .edit()
                .putString(LOGIN, login)
                .apply();
    }

    @Override
    @NonNull
    public String getLogin() {
        return preferences.getString(LOGIN, "");
    }

}
      