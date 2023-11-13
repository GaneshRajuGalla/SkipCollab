// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
typealias PlatformDateInterval = java.time.Duration

class DateInterval: Comparable<DateInterval>, MutableStruct {
    internal var platformValue: java.time.Duration
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    internal constructor(platformValue: java.time.Duration) {
        this.platformValue = platformValue
    }

    val description: String
        get() = platformValue.description

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val start: Date
        get() {
            return fatalError("SKIP TODO")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val end: Date
        get() {
            return fatalError("SKIP TODO")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val duration: Double
        get() {
            return fatalError("SKIP TODO")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor() {
        this.platformValue = SkipCrash("TODO: PlatformDateInterval")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(start: Date, end: Date) {
        this.platformValue = SkipCrash("TODO: PlatformDateInterval")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(start: Date, duration: Double) {
        this.platformValue = SkipCrash("TODO: PlatformDateInterval")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun compare(dateInterval: DateInterval): ComparisonResult {
        return fatalError("SKIP TODO")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun intersects(dateInterval: DateInterval): Boolean {
        return fatalError("SKIP TODO")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun intersection(with: DateInterval): DateInterval? {
        val dateInterval = with
        return fatalError("SKIP TODO")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun contains(date: Date): Boolean {
        return fatalError("SKIP TODO")
    }

    override fun equals(other: Any?): Boolean {
        if (other !is DateInterval) {
            return false
        }
        val lhs = this
        val rhs = other
        return fatalError("SKIP TODO")
    }

    override fun compareTo(other: DateInterval): Int {
        if (this == other) return 0
        fun islessthan(lhs: DateInterval, rhs: DateInterval): Boolean {
            return fatalError("SKIP TODO")
        }
        return if (islessthan(this, other)) -1 else 1
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as DateInterval
        this.platformValue = copy.platformValue
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = DateInterval(this as MutableStruct)

    override fun toString(): String = description

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, platformValue)
        return result
    }

    companion object {
    }
}
