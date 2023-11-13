// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

class MatchedGeometryProperties: OptionSet<MatchedGeometryProperties, Int>, Sendable {
    override var rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override val rawvaluelong: ULong
        get() = ULong(rawValue)
    override fun makeoptionset(rawvaluelong: ULong): MatchedGeometryProperties = MatchedGeometryProperties(rawValue = Int(rawvaluelong))
    override fun assignoptionset(target: MatchedGeometryProperties): Unit = assignfrom(target)

    private fun assignfrom(target: MatchedGeometryProperties) {
        this.rawValue = target.rawValue
    }

    companion object {

        val position = MatchedGeometryProperties(rawValue = 1 shl 0)
        val size = MatchedGeometryProperties(rawValue = 1 shl 1)
        val frame = MatchedGeometryProperties(rawValue = 1 shl 2)

        fun of(vararg options: MatchedGeometryProperties): MatchedGeometryProperties {
            val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
            return MatchedGeometryProperties(rawValue = value)
        }
    }
}
