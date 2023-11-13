// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

open class ISO8601DateFormatter: DateFormatter {
    constructor(): super() {
        this.dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX"
        this.timeZone = (try { TimeZone(identifier = "UTC") } catch (_: NullReturnException) { null })
    }

    // TODO: handle options
    open var formatOptions: ISO8601DateFormatter.Options = Options(rawValue = 0U)

    class Options: OptionSet<ISO8601DateFormatter.Options, UInt> {
        override var rawValue: UInt
        constructor(rawValue: UInt) {
            this.rawValue = rawValue
        }

        override val rawvaluelong: ULong
            get() = ULong(rawValue)
        override fun makeoptionset(rawvaluelong: ULong): ISO8601DateFormatter.Options = Options(rawValue = UInt(rawvaluelong))
        override fun assignoptionset(target: ISO8601DateFormatter.Options): Unit = assignfrom(target)

        private fun assignfrom(target: ISO8601DateFormatter.Options) {
            this.rawValue = target.rawValue
        }

        companion object {
            val withYear = ISO8601DateFormatter.Options(rawValue = 1U shl 0)
            val withMonth = ISO8601DateFormatter.Options(rawValue = 1U shl 1)
            val withWeekOfYear = ISO8601DateFormatter.Options(rawValue = 1U shl 2)
            val withDay = ISO8601DateFormatter.Options(rawValue = 1U shl 4)
            val withTime = ISO8601DateFormatter.Options(rawValue = 1U shl 5)
            val withTimeZone = ISO8601DateFormatter.Options(rawValue = 1U shl 6)
            val withSpaceBetweenDateAndTime = ISO8601DateFormatter.Options(rawValue = 1U shl 7)
            val withDashSeparatorInDate = ISO8601DateFormatter.Options(rawValue = 1U shl 8)
            val withColonSeparatorInTime = ISO8601DateFormatter.Options(rawValue = 1U shl 9)
            val withColonSeparatorInTimeZone = ISO8601DateFormatter.Options(rawValue = 1U shl 10)
            val withFractionalSeconds = ISO8601DateFormatter.Options(rawValue = 1U shl 11)
            val withFullDate = ISO8601DateFormatter.Options(rawValue = withYear.rawValue + withMonth.rawValue + withDay.rawValue + withDashSeparatorInDate.rawValue)
            val withFullTime = ISO8601DateFormatter.Options(rawValue = withTime.rawValue + withTimeZone.rawValue + withColonSeparatorInTime.rawValue + withColonSeparatorInTimeZone.rawValue)
            val withInternetDateTime = ISO8601DateFormatter.Options(rawValue = withFullDate.rawValue + withFullTime.rawValue)

            fun of(vararg options: ISO8601DateFormatter.Options): ISO8601DateFormatter.Options {
                val value = options.fold(UInt(0)) { result, option -> result or option.rawValue }
                return Options(rawValue = value)
            }
        }
    }

    companion object {
    }
}
