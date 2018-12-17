package com.alekseyvalyakin.roleplaysystem.utils

fun MutableList<String>.addIfNotContainsAndNotBlank(item: String): Boolean {
    if (item.isNotBlank() && !this.contains(item)) {
        return add(item)
    }

    return false
}