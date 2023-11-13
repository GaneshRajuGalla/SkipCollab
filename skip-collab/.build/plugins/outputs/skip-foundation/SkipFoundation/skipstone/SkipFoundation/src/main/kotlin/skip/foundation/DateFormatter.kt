// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
typealias PlatformDateFormatter = java.text.SimpleDateFormat

open class DateFormatter {
    internal open var platformValue: java.text.SimpleDateFormat

    internal constructor(platformValue: java.text.SimpleDateFormat) {
        this.platformValue = platformValue
    }

    constructor() {
        this.platformValue = java.text.SimpleDateFormat()
        this.isLenient = false // SimpleDateFormat is lenient by default
    }

    open val description: String
        get() = platformValue.description

    open var isLenient: Boolean
        get() = platformValue.isLenient
        set(newValue) {
            platformValue.isLenient = newValue
        }

    open var dateFormat: String
        get() = platformValue.toPattern()
        set(newValue) {
            platformValue.applyPattern(newValue)
        }

    open fun setLocalizedDateFormatFromTemplate(dateFormatTemplate: String): Unit = platformValue.applyLocalizedPattern(dateFormatTemplate)

    open var timeZone: TimeZone?
        get() {
            val matchtarget_0 = platformValue.timeZone
            if (matchtarget_0 != null) {
                val rawTimeZone = matchtarget_0
                return TimeZone(platformValue = rawTimeZone).sref({ this.timeZone = it })
            } else {
                return TimeZone.current.sref({ this.timeZone = it })
            }

            fatalError("unreachable") // “A 'return' expression required in a function with a block body ('{...}'). If you got this error after the compiler update, then it's most likely due to a fix of a bug introduced in 1.3.0 (see KT-28061 for details)”
        }
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            platformValue.timeZone = newValue?.platformValue ?: TimeZone.current.platformValue
        }

    // SimpleDateFormat holds a locale, but it is not readable
    private var _locale: Locale? = null

    open var locale: Locale?
        get() = this._locale ?: Locale.current
        set(newValue) {
            // need to make a whole new SimpleDateFormat with the locale, since the instance does not provide access to the locale that was used to initialize it
            if (newValue != null) {
                var formatter = java.text.SimpleDateFormat(this.platformValue.toPattern(), newValue.platformValue)
                formatter.timeZone = this.timeZone?.platformValue
                this.platformValue = formatter
                this._locale = newValue
            }
        }

    open var calendar: Calendar?
        get() = Calendar(platformValue = platformValue.calendar).sref({ this.calendar = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            platformValue.calendar = newValue?.platformValue
        }

    open fun date(from: String): Date? {
        val string = from
        val matchtarget_1 = try { platformValue.parse(string) } catch (_: Throwable) { null }
        if (matchtarget_1 != null) {
            val date = matchtarget_1
            return Date(platformValue = date)
        } else {
            return null
        }
    }

    open fun string(from: Date): String {
        val date = from
        return platformValue.format(date.platformValue)
    }

    companion object {

        fun dateFormat(fromTemplate: String, options: Int, locale: Locale?): String? {
            val fmt = DateFormatter()
            fmt.locale = locale
            fmt.setLocalizedDateFormatFromTemplate(fromTemplate)
            return fmt.platformValue.toLocalizedPattern()
        }
    }
}
