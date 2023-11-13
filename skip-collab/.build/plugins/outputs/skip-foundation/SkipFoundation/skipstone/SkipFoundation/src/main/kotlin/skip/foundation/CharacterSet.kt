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
internal typealias PlatformCharacterSet = Set<Char>

class CharacterSet: MutableStruct {
    internal var platformValue: Set<Char>
        get() = field.sref({ this.platformValue = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }

    internal constructor(platformValue: Set<Char>) {
        this.platformValue = platformValue
    }

    constructor() {
        this.platformValue = Set<Char>()
    }

    val description: String
        get() = platformValue.description

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(charactersIn: IntRange) {
        val range = charactersIn
        this.platformValue = SkipCrash("TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(charactersIn: ClosedRange<Unicode.Scalar>) {
        val range = charactersIn
        this.platformValue = SkipCrash("TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(charactersIn: String, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        val string = charactersIn
        this.platformValue = SkipCrash("TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(bitmapRepresentation: Data) {
        val data = bitmapRepresentation
        this.platformValue = SkipCrash("TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(contentsOfFile: String) {
        val file = contentsOfFile
        this.platformValue = SkipCrash("SKIP TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val bitmapRepresentation: Data
        get() {
            return fatalError("SKIP TODO: CharacterSet")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val inverted: CharacterSet
        get() {
            return fatalError("SKIP TODO: CharacterSet")
        }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun hasMember(inPlane: UByte): Boolean {
        val plane = inPlane
        return fatalError("SKIP TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun insert(charactersIn: IntRange) {
        val range = charactersIn
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun insert(charactersIn: ClosedRange<Unicode.Scalar>) {
        val range = charactersIn
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun remove(charactersIn: IntRange) {
        val range = charactersIn
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun remove(charactersIn: ClosedRange<Unicode.Scalar>) {
        val range = charactersIn
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun insert(charactersIn: String) {
        val string = charactersIn
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun remove(charactersIn: String) {
        val string = charactersIn
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun invert() {
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun insert(character: Unicode.Scalar): Tuple2<Boolean, Unicode.Scalar> {
        willmutate()
        try {
            return fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun update(with: Unicode.Scalar): Unicode.Scalar? {
        val character = with
        willmutate()
        try {
            return fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun remove(character: Unicode.Scalar): Unicode.Scalar? {
        willmutate()
        try {
            return fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun contains(member: Unicode.Scalar): Boolean {
        return fatalError("SKIP TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun union(other: CharacterSet): CharacterSet {
        return fatalError("SKIP TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun formUnion(other: CharacterSet) {
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun intersection(other: CharacterSet): CharacterSet {
        return fatalError("SKIP TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun formIntersection(other: CharacterSet) {
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun subtracting(other: CharacterSet): CharacterSet {
        return fatalError("SKIP TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun subtract(other: CharacterSet) {
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun symmetricDifference(other: CharacterSet): CharacterSet {
        return fatalError("SKIP TODO: CharacterSet")
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun formSymmetricDifference(other: CharacterSet) {
        willmutate()
        try {
            fatalError("SKIP TODO: CharacterSet")
        } finally {
            didmutate()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun isSuperset(of: CharacterSet): Boolean {
        val other = of
        return fatalError("SKIP TODO: CharacterSet")
    }


    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as CharacterSet
        this.platformValue = copy.platformValue
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = CharacterSet(this as MutableStruct)

    override fun equals(other: Any?): Boolean {
        if (other !is CharacterSet) return false
        return platformValue == other.platformValue
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, platformValue)
        return result
    }

    companion object {

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val controlCharacters: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        val whitespaces: CharacterSet
            get() {
                // TODO: Actual values
                return CharacterSet(platformValue = setOf(' ', '\t'))
            }

        val whitespacesAndNewlines: CharacterSet
            get() {
                // TODO: Actual values
                return CharacterSet(platformValue = setOf(' ', '\t', '\n', '\r'))
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val decimalDigits: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val letters: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        val lowercaseLetters: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        val uppercaseLetters: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val nonBaseCharacters: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val alphanumerics: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val decomposables: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val illegalCharacters: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val punctuationCharacters: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val capitalizedLetters: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val symbols: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val newlines: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val urlUserAllowed: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val urlPasswordAllowed: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val urlHostAllowed: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val urlPathAllowed: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val urlQueryAllowed: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val urlFragmentAllowed: CharacterSet
            get() {
                return fatalError("SKIP TODO: CharacterSet")
            }
    }
}
