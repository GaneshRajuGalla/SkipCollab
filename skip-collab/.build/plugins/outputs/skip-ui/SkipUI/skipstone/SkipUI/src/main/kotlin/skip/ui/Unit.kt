// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

class UnitPoint: Sendable, MutableStruct {
    var x: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var y: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(x: Double = 0.0, y: Double = 0.0) {
        this.x = x
        this.y = y
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = UnitPoint(x, y)

    override fun equals(other: Any?): Boolean {
        if (other !is UnitPoint) return false
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, x)
        result = Hasher.combine(result, y)
        return result
    }

    companion object {

        val zero = UnitPoint(x = 0.0, y = 0.0)
        val center = UnitPoint(x = 0.5, y = 0.5)
        val leading = UnitPoint(x = 0.0, y = 0.5)
        val trailing = UnitPoint(x = 1.0, y = 0.5)
        val top = UnitPoint(x = 0.5, y = 0.0)
        val bottom = UnitPoint(x = 0.5, y = 1.0)
        val topLeading = UnitPoint(x = 0.0, y = 0.0)
        val topTrailing = UnitPoint(x = 1.0, y = 0.0)
        val bottomLeading = UnitPoint(x = 0.0, y = 1.0)
        val bottomTrailing = UnitPoint(x = 1.0, y = 1.0)
    }
}

