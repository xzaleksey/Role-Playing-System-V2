package com.alekseyvalyakin.roleplaysystem.utils

object StringUtils {
    const val EMPTY_STRING = ""
    const val UNDEFINED = "UNDEFINED"
    val FORMAT_SLASHES = "/%s/"

    fun formatWithCount(s: String, count: Int): String {
        return s + " (" + count.toString() + ")"
    }

    fun formatWithSlashes(s: String): String {
        return String.format(FORMAT_SLASHES, s)
    }

    fun formatRightSlash(s: String): String {
        return "$s/"
    }

    fun isEmpty(text: CharSequence?): Boolean {
        return text == null || text.length == 0
    }

    fun areEqual(s: String?, s1: String?): Boolean {
        return if (s == null) s1 == null else s == s1
    }

    fun usernameFromEmail(email: String?): String {
        if (email == null) {
            return StringUtils.EMPTY_STRING
        }

        return if (email.contains("@")) {
            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        } else {
            email
        }
    }
}