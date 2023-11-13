// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

class SubmitTriggers: OptionSet<SubmitTriggers, Int>, Sendable {
    override var rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override val rawvaluelong: ULong
        get() = ULong(rawValue)
    override fun makeoptionset(rawvaluelong: ULong): SubmitTriggers = SubmitTriggers(rawValue = Int(rawvaluelong))
    override fun assignoptionset(target: SubmitTriggers): Unit = assignfrom(target)

    private fun assignfrom(target: SubmitTriggers) {
        this.rawValue = target.rawValue
    }

    companion object {

        val text = SubmitTriggers(rawValue = 1 shl 0)
        val search = SubmitTriggers(rawValue = 1 shl 1)

        fun of(vararg options: SubmitTriggers): SubmitTriggers {
            val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
            return SubmitTriggers(rawValue = value)
        }
    }
}
