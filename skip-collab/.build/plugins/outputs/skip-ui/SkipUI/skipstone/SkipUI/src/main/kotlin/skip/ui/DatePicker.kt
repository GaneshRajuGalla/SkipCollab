// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import skip.foundation.*

// Erase the generic Label to facilitate specialized constructor support.
class DatePicker: View {

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(selection: Binding<Date>, displayedComponents: DatePickerComponents = DatePickerComponents.of(DatePickerComponents.hourAndMinute, DatePickerComponents.date), label: () -> View) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(selection: Binding<Date>, in_: IntRange, displayedComponents: DatePickerComponents = DatePickerComponents.of(DatePickerComponents.hourAndMinute, DatePickerComponents.date), label: () -> View) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, selection: Binding<Date>, displayedComponents: DatePickerComponents = DatePickerComponents.of(DatePickerComponents.hourAndMinute, DatePickerComponents.date)) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, selection: Binding<Date>, in_: IntRange, displayedComponents: DatePickerComponents = DatePickerComponents.of(DatePickerComponents.hourAndMinute, DatePickerComponents.date)) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, selection: Binding<Date>, displayedComponents: DatePickerComponents = DatePickerComponents.of(DatePickerComponents.hourAndMinute, DatePickerComponents.date)) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, selection: Binding<Date>, in_: IntRange, displayedComponents: DatePickerComponents = DatePickerComponents.of(DatePickerComponents.hourAndMinute, DatePickerComponents.date)) {
    }


    companion object {
    }
}

class DatePickerComponents: OptionSet<DatePickerComponents, Int>, Sendable {
    override var rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override val rawvaluelong: ULong
        get() = ULong(rawValue)
    override fun makeoptionset(rawvaluelong: ULong): DatePickerComponents = DatePickerComponents(rawValue = Int(rawvaluelong))
    override fun assignoptionset(target: DatePickerComponents): Unit = assignfrom(target)

    private fun assignfrom(target: DatePickerComponents) {
        this.rawValue = target.rawValue
    }

    companion object {

        val hourAndMinute = DatePickerComponents(rawValue = 1 shl 0)
        val date = DatePickerComponents(rawValue = 1 shl 1)

        fun of(vararg options: DatePickerComponents): DatePickerComponents {
            val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
            return DatePickerComponents(rawValue = value)
        }
    }
}

// Model `DatePickerStyle` as a struct. Kotlin does not support static members of protocols
class DatePickerStyle: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is DatePickerStyle) return false
        return rawValue == other.rawValue
    }

    companion object {

        val automatic = ButtonStyle(rawValue = 0)

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val graphical = ButtonStyle(rawValue = 1)

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val wheel = ButtonStyle(rawValue = 2)

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val compact = ButtonStyle(rawValue = 3)
    }
}

