// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

class SafeAreaRegions: OptionSet<SafeAreaRegions, Int>, Sendable {
    override var rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override val rawvaluelong: ULong
        get() = ULong(rawValue)
    override fun makeoptionset(rawvaluelong: ULong): SafeAreaRegions = SafeAreaRegions(rawValue = Int(rawvaluelong))
    override fun assignoptionset(target: SafeAreaRegions): Unit = assignfrom(target)

    private fun assignfrom(target: SafeAreaRegions) {
        this.rawValue = target.rawValue
    }

    companion object {

        val container = SafeAreaRegions(rawValue = 1)
        val keyboard = SafeAreaRegions(rawValue = 2)
        val all = SafeAreaRegions(rawValue = 3)

        fun of(vararg options: SafeAreaRegions): SafeAreaRegions {
            val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
            return SafeAreaRegions(rawValue = value)
        }
    }
}
