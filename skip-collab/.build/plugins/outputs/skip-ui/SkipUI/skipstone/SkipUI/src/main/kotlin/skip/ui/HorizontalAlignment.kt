// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*
import skip.lib.Sequence


class HorizontalAlignment: Sendable {
    internal val key: String

    internal constructor(key: String) {
        this.key = key
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(id: Any) {
        key = ""
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun combineExplicit(values: Sequence<Double?>): Double? {
        fatalError()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is HorizontalAlignment) return false
        return key == other.key
    }

    companion object {

        val leading: HorizontalAlignment = HorizontalAlignment(key = "leading")
        val center: HorizontalAlignment = HorizontalAlignment(key = "center")
        val trailing: HorizontalAlignment = HorizontalAlignment(key = "trailing")
        val listRowSeparatorLeading = HorizontalAlignment(key = "listRowSeparatorLeading")
        val listRowSeparatorTrailing = HorizontalAlignment(key = "listRowSeparatorTrailing")
    }
}
