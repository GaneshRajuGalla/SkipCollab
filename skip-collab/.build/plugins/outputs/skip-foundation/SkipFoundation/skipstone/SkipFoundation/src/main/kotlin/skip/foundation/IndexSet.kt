// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*
import skip.lib.Set

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
internal typealias PlatformIndexSet = skip.lib.Set<Int>
internal typealias IndexSetElement = Int
internal typealias IndexSetIndex = Int

class IndexSet: MutableStruct {
    internal var platformValue: skip.lib.Set<Int>
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    internal constructor(platformValue: skip.lib.Set<Int>) {
        this.platformValue = platformValue
    }

    constructor() {
        this.platformValue = skip.lib.Set<Int>()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val count: Int
        get() {
            fatalError()
        }

    val description: String
        get() = platformValue.description

    // TODO: fill in IndexSet stub methods

    fun kotlin(nocopy: Boolean = false): skip.lib.Set<Int> = if (nocopy) platformValue else platformValue.sref()

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as IndexSet
        this.platformValue = copy.platformValue
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = IndexSet(this as MutableStruct)

    override fun toString(): String = description

    override fun equals(other: Any?): Boolean {
        if (other !is IndexSet) return false
        return platformValue == other.platformValue
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, platformValue)
        return result
    }

    companion object {
    }
}

