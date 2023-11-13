// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.

typealias PlatformPseudoRandomNumberGenerator = java.util.Random
typealias PlatformSystemRandomNumberGenerator = java.security.SecureRandom

interface RandomNumberGenerator {
    fun next(): ULong
}

class SystemRandomNumberGenerator: RawRepresentable<java.security.SecureRandom>, RandomNumberGenerator, MutableStruct {
    override var rawValue: java.security.SecureRandom
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(rawValue: java.security.SecureRandom) {
        this.rawValue = rawValue
    }

    constructor(rawValue: java.security.SecureRandom = java.security.SecureRandom(), @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        this.rawValue = rawValue
    }

    fun nextLong(): Long = rawValue.nextLong()

    fun nextInt(): Int = rawValue.nextInt()

    override fun next(): ULong = rawValue.nextLong().toULong()

    fun nextBoolean(): Boolean = rawValue.nextBoolean()

    fun nextUUID(): UUID = UUID(mostSigBits = rawValue.nextLong(), leastSigBits = rawValue.nextLong())

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as SystemRandomNumberGenerator
        this.rawValue = copy.rawValue
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = SystemRandomNumberGenerator(this as MutableStruct)

    companion object {
    }
}


class PseudoRandomNumberGenerator: RawRepresentable<java.util.Random>, MutableStruct {
    override var rawValue: java.util.Random
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(rawValue: java.util.Random) {
        this.rawValue = rawValue
    }

    constructor(rawValue: java.util.Random = java.util.Random(), @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        this.rawValue = rawValue
    }

    fun nextLong(): Long = rawValue.nextLong()

    fun nextInt(): Int = rawValue.nextInt()

    fun next(): ULong = rawValue.nextLong().toULong()

    fun nextBoolean(): Boolean = rawValue.nextBoolean()

    fun nextUUID(): UUID = UUID(mostSigBits = rawValue.nextLong(), leastSigBits = rawValue.nextLong())

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as PseudoRandomNumberGenerator
        this.rawValue = copy.rawValue
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = PseudoRandomNumberGenerator(this as MutableStruct)

    companion object {

        fun seeded(seed: Long): PseudoRandomNumberGenerator = PseudoRandomNumberGenerator(rawValue = java.util.Random(seed))
    }
}

