// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*
import skip.lib.Sequence


class VerticalAlignment: Sendable {
    internal val key: String

    internal constructor(key: String) {
        this.key = key
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(id: Any) {
        key = ""
    }

    fun combineExplicit(values: Sequence<Double?>): Double? {
        fatalError()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is VerticalAlignment) return false
        return key == other.key
    }

    companion object {

        val top = VerticalAlignment(key = "top")
        val center = VerticalAlignment(key = "center")
        val bottom = VerticalAlignment(key = "bottom")
        val firstTextBaseline = VerticalAlignment(key = "firstTextBaseline")
        val lastTextBaseline = VerticalAlignment(key = "lastTextBaseline")
    }
}
