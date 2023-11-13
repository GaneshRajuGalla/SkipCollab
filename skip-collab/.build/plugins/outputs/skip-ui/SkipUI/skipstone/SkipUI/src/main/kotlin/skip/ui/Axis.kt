// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*
import skip.lib.Array
import skip.lib.Set

enum class Axis(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): CaseIterable, Sendable, RawRepresentable<Int> {
    horizontal(1),
    vertical(2);

    class Set: OptionSet<Axis.Set, Int>, Sendable {
        override var rawValue: Int

        constructor(rawValue: Int) {
            this.rawValue = rawValue
        }

        constructor(axis: Axis) {
            this.rawValue = axis.rawValue
        }

        override val rawvaluelong: ULong
            get() = ULong(rawValue)
        override fun makeoptionset(rawvaluelong: ULong): Axis.Set = Set(rawValue = Int(rawvaluelong))
        override fun assignoptionset(target: Axis.Set): Unit = assignfrom(target)

        private fun assignfrom(target: Axis.Set) {
            this.rawValue = target.rawValue
        }

        companion object {

            val horizontal: Axis.Set = Axis.Set(Axis.horizontal)
            val vertical: Axis.Set = Axis.Set(Axis.vertical)

            fun of(vararg options: Axis.Set): Axis.Set {
                val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
                return Set(rawValue = value)
            }
        }
    }

    companion object {
        val allCases: Array<Axis>
            get() = arrayOf(horizontal, vertical)
    }
}

fun Axis(rawValue: Int): Axis? {
    return when (rawValue) {
        1 -> Axis.horizontal
        2 -> Axis.vertical
        else -> null
    }
}
