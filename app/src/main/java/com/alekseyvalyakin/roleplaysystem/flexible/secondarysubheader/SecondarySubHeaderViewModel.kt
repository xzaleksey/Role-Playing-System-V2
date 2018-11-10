package com.alekseyvalyakin.roleplaysystem.flexible.secondarysubheader

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.utils.dip
import com.alekseyvalyakin.roleplaysystem.views.SecondarySubheaderView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import java.io.Serializable

class SecondarySubHeaderViewModel(
        val title: String,
        val color: Int = 0,
        val isDrawBottomDivider: Boolean = false,
        val isDrawTopDivider: Boolean = false,
        val marginLeft: Int = 24.dip()
) : AbstractFlexibleItem<SecondarySubHeaderViewHolder>(), Serializable {

    override fun createViewHolder(adapter: FlexibleAdapter<*>?, inflater: LayoutInflater?,
                                  parent: ViewGroup): SecondarySubHeaderViewHolder {
        return SecondarySubHeaderViewHolder(SecondarySubheaderView(parent.context))
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holderSecondary: SecondarySubHeaderViewHolder, position: Int,
                                payloads: List<*>?) {
        holderSecondary.bind(this)
    }

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.SUB_HEADER_SECONDARY
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as SecondarySubHeaderViewModel

        if (color != that.color) return false
        if (isDrawBottomDivider != that.isDrawBottomDivider) return false
        if (isDrawTopDivider != that.isDrawTopDivider) return false
        return title == that.title
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + color
        result = 31 * result + if (isDrawBottomDivider) 1 else 0
        result = 31 * result + if (isDrawTopDivider) 1 else 0
        return result
    }
}
      