package com.alekseyvalyakin.roleplaysystem.base.filter

import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import java.io.Serializable

data class FilterModel constructor(
        val query: String = StringUtils.EMPTY_STRING,
        val previousQuery: String = StringUtils.EMPTY_STRING
) : Serializable {

    fun isEmpty(): Boolean = query.isBlank()

    fun isCleared(): Boolean = previousQuery.isNotEmpty() && query.isEmpty()

}