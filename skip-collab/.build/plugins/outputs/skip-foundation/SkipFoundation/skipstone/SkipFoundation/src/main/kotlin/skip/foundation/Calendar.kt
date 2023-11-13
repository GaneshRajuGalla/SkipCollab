// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*
import skip.lib.Array
import skip.lib.Set

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
typealias PlatformCalendar = java.util.Calendar
typealias PlatformCalendarComponent = Calendar.Component
typealias PlatformCalendarIdentifier = Calendar.Identifier

// Needed to expose `clone`:
fun java.util.Calendar.clone(): java.util.Calendar { return this.clone() as java.util.Calendar }

/// A definition of the relationships between calendar units and absolute points in time, providing features for calculation and comparison of dates.
class Calendar: MutableStruct {
    internal var platformValue: java.util.Calendar
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var locale: Locale
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    internal constructor(platformValue: java.util.Calendar) {
        this.platformValue = platformValue
        this.locale = Locale.current
    }

    internal constructor(platformValue: java.util.Calendar, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        this.platformValue = platformValue
        this.locale = Locale.current
    }

    internal constructor(identifier: Calendar.Identifier) {
        when (identifier) {
            Calendar.Identifier.gregorian -> this.platformValue = java.util.GregorianCalendar()
            else -> {
                // TODO: how to support the other calendars?
                fatalError("Skip: unsupported calendar identifier ${identifier}")
            }
        }
        this.locale = Locale.current
    }

    internal val identifier: Calendar.Identifier
        get() {
            // TODO: non-gregorian calendar
            if (gregorianCalendar != null) {
                return Calendar.Identifier.gregorian
            } else {
                return Calendar.Identifier.iso8601
            }
        }

    internal fun toDate(): Date = Date(platformValue = platformValue.getTime())

    private val dateFormatSymbols: java.text.DateFormatSymbols
        get() = java.text.DateFormatSymbols.getInstance(locale.platformValue)

    private val gregorianCalendar: java.util.GregorianCalendar?
        get() = platformValue as? java.util.GregorianCalendar

    val amSymbol: String
        get() = dateFormatSymbols.getAmPmStrings()[0]

    val pmSymbol: String
        get() = dateFormatSymbols.getAmPmStrings()[1]

    val eraSymbols: Array<String>
        get() = Array(dateFormatSymbols.getEras().toList())

    val monthSymbols: Array<String>
        get() {
            // The java.text.DateFormatSymbols.getInstance().getMonths() method in Java returns an array of 13 symbols because it includes both the 12 months of the year and an additional symbol
            // some documentation says the blank symbol is at index 0, but other tests show it at the end, so just pare it out
            return Array(dateFormatSymbols.getMonths().toList()).filter({ it -> it?.isEmpty == false })
        }

    val shortMonthSymbols: Array<String>
        get() {
            return Array(dateFormatSymbols.getShortMonths().toList()).filter({ it -> it?.isEmpty == false })
        }

    val weekdaySymbols: Array<String>
        get() {
            return Array(dateFormatSymbols.getWeekdays().toList()).filter({ it -> it?.isEmpty == false })
        }

    val shortWeekdaySymbols: Array<String>
        get() {
            return Array(dateFormatSymbols.getShortWeekdays().toList()).filter({ it -> it?.isEmpty == false })
        }


    fun date(from: DateComponents): Date? {
        val components = from
        // TODO: Need to set `this` calendar in the components.calendar
        return Date(platformValue = components.createCalendarComponents().getTime())
    }

    fun dateComponents(in_: TimeZone? = null, from: Date): DateComponents {
        val zone = in_
        val date = from
        return DateComponents(fromCalendar = this, in_ = zone ?: this.timeZone, from = date)
    }

    fun dateComponents(components: Set<Calendar.Component>, from: Date, to: Date): DateComponents {
        val start = from
        val end = to
        return DateComponents(fromCalendar = this, in_ = null, from = start, to = end)
    }

    fun dateComponents(components: Set<Calendar.Component>, from: Date): DateComponents {
        val date = from
        return DateComponents(fromCalendar = this, in_ = null, from = date, with = components)
    }

    fun date(byAdding: DateComponents, to: Date, wrappingComponents: Boolean = false): Date? {
        val components = byAdding
        val date = to
        var comps = DateComponents(fromCalendar = this, in_ = this.timeZone, from = date)
        comps.add(components)
        return date(from = comps)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun date(byAdding: Calendar.Component, value: Int, to: Date, wrappingComponents: Boolean = false): Date? {
        val component = byAdding
        val date = to
        return fatalError("TODO: Skip Calendar.date(byAdding:Calendar.Component)")
    }

    fun isDateInWeekend(date: Date): Boolean {
        val components = dateComponents(from = date)
        return components.weekday == java.util.Calendar.SATURDAY || components.weekday == java.util.Calendar.SUNDAY
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun isDate(date1: Date, inSameDayAs: Date): Boolean {
        val date2 = inSameDayAs
        return fatalError("TODO: Skip Calendar.isDate(:inSameDayAs:)")
    }

    fun isDateInToday(date: Date): Boolean {
        // return isDate(date, inSameDayAs: Date())
        return fatalError("TODO: Skip Calendar.isDate(:inSameDayAs:)")
    }

    fun isDateInTomorrow(date: Date): Boolean {
        val matchtarget_0 = date(byAdding = DateComponents(day = -1), to = Date())
        if (matchtarget_0 != null) {
            val tomorrow = matchtarget_0
            // return isDate(date, inSameDayAs: tomorrow)
            fatalError("TODO: Skip Calendar.isDate(:inSameDayAs:)")
        } else {
            return false
        }
    }

    fun isDateInYesterday(date: Date): Boolean {
        val matchtarget_1 = date(byAdding = DateComponents(day = -1), to = Date())
        if (matchtarget_1 != null) {
            val yesterday = matchtarget_1
            // return isDate(date, inSameDayAs: yesterday)
            fatalError("TODO: Skip Calendar.isDate(:inSameDayAs:)")
        } else {
            return false
        }
    }

    var timeZone: TimeZone
        get() = TimeZone(platformValue.getTimeZone()).sref({ this.timeZone = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            platformValue.setTimeZone(newValue.platformValue)
        }

    val description: String
        get() = platformValue.description

    enum class Component: Sendable {
        era,
        year,
        month,
        day,
        hour,
        minute,
        second,
        weekday,
        weekdayOrdinal,
        quarter,
        weekOfMonth,
        weekOfYear,
        yearForWeekOfYear,
        nanosecond,
        calendar,
        timeZone;


        companion object {
        }
    }

    /// Calendar supports many different kinds of calendars. Each is identified by an identifier here.
    enum class Identifier: Sendable {
        /// The common calendar in Europe, the Western Hemisphere, and elsewhere.
        gregorian,
        buddhist,
        chinese,
        coptic,
        ethiopicAmeteMihret,
        ethiopicAmeteAlem,
        hebrew,
        iso8601,
        indian,
        islamic,
        islamicCivil,
        japanese,
        persian,
        republicOfChina,
        islamicTabular,
        islamicUmmAlQura;

        companion object {
        }
    }

    fun kotlin(nocopy: Boolean = false): java.util.Calendar = if (nocopy) platformValue else platformValue.clone() as java.util.Calendar

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as Calendar
        this.platformValue = copy.platformValue
        this.locale = copy.locale
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = Calendar(this as MutableStruct)

    override fun toString(): String = description

    override fun equals(other: Any?): Boolean {
        if (other !is Calendar) return false
        return platformValue == other.platformValue && locale == other.locale
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, platformValue)
        result = Hasher.combine(result, locale)
        return result
    }

    companion object {

        val current: Calendar
            get() = Calendar(platformValue = java.util.Calendar.getInstance())
    }
}


internal fun java.util.Calendar.swift(nocopy: Boolean = false): Calendar {
    val platformValue = if (nocopy) this else clone() as java.util.Calendar
    return Calendar(platformValue = platformValue)
}

// Shims for testing
internal open class NSCalendar: java.lang.Object() {
    internal class Options {
    }

    internal enum class Unit {
        era,
        year,
        month,
        day,
        hour,
        minute,
        second,
        weekday,
        weekdayOrdinal,
        quarter,
        weekOfMonth,
        weekOfYear,
        yearForWeekOfYear,
        nanosecond,
        calendar,
        timeZone;
    }

    internal enum class Identifier {
        gregorian,
        buddhist,
        chinese,
        coptic,
        ethiopicAmeteMihret,
        ethiopicAmeteAlem,
        hebrew,
        ISO8601,
        indian,
        islamic,
        islamicCivil,
        japanese,
        persian,
        republicOfChina,
        islamicTabular,
        islamicUmmAlQura;
    }
}
