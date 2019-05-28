package com.alekseyvalyakin.roleplaysystem.utils

fun MutableList<String>.addIfNotContainsAndNotBlank(item: String): Boolean {
    if (item.isNotBlank() && !this.contains(item)) {
        return add(item)
    }

    return false
}

fun <E> MutableCollection<E>.removeIfFiltered(filter: (element: E) -> Boolean): Boolean {
    var removed = false
    val each = iterator()
    while (each.hasNext()) {
        if (filter(each.next())) {
            each.remove()
            removed = true
        }
    }
    return removed
}