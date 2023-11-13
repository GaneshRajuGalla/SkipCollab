// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

class EditActions: OptionSet<EditActions, Int>, Sendable {
    override var rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override val rawvaluelong: ULong
        get() = ULong(rawValue)
    override fun makeoptionset(rawvaluelong: ULong): EditActions = EditActions(rawValue = Int(rawvaluelong))
    override fun assignoptionset(target: EditActions): Unit = assignfrom(target)

    private fun assignfrom(target: EditActions) {
        this.rawValue = target.rawValue
    }

    companion object {

        val move = EditActions(rawValue = 1)
        val delete = EditActions(rawValue = 2)
        val all = EditActions(rawValue = 3)

        fun of(vararg options: EditActions): EditActions {
            val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
            return EditActions(rawValue = value)
        }
    }
}
