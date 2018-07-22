package com.alekseyvalyakin.roleplaysystem.utils;

import android.text.TextUtils;

public class ValidationUtils {
    private static final int DEFAULT_MIN_LENGTH = 6;

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidLength(CharSequence target) {
        return target.length() >= DEFAULT_MIN_LENGTH;
    }
}
      