// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

interface ShapeStyle: Sendable {
}

class FillStyle: Sendable, MutableStruct {
    var isEOFilled: Boolean
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var isAntialiased: Boolean
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(eoFill: Boolean = false, antialiased: Boolean = true) {
        this.isEOFilled = eoFill
        this.isAntialiased = antialiased
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as FillStyle
        this.isEOFilled = copy.isEOFilled
        this.isAntialiased = copy.isAntialiased
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = FillStyle(this as MutableStruct)

    override fun equals(other: Any?): Boolean {
        if (other !is FillStyle) return false
        return isEOFilled == other.isEOFilled && isAntialiased == other.isAntialiased
    }

    companion object {
    }
}

enum class RoundedCornerStyle: Sendable {
    circular,
    continuous;

    companion object {
    }
}

