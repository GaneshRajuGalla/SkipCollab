// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.lib

typealias CGFloat = Double

class CGPoint: MutableStruct {
    var x: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var y: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(x: Double = 0.0, y: Double = 0.0) {
        this.x = x
        this.y = y
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as CGPoint
        this.x = copy.x
        this.y = copy.y
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = CGPoint(this as MutableStruct)

    override fun equals(other: Any?): Boolean {
        if (other !is CGPoint) return false
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, x)
        result = Hasher.combine(result, y)
        return result
    }

    companion object {
        val zero = CGPoint()
    }
}

class CGSize: MutableStruct {
    var width: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    var height: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(width: Double = 0.0, height: Double = 0.0) {
        this.width = width
        this.height = height
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as CGSize
        this.width = copy.width
        this.height = copy.height
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = CGSize(this as MutableStruct)

    override fun equals(other: Any?): Boolean {
        if (other !is CGSize) return false
        return width == other.width && height == other.height
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, width)
        result = Hasher.combine(result, height)
        return result
    }

    companion object {
        val zero = CGSize()
    }
}

class CGRect: MutableStruct {
    var origin: CGPoint
        get() = field.sref({ this.origin = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    var size: CGSize
        get() = field.sref({ this.size = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(origin: CGPoint = CGPoint.zero, size: CGSize = CGSize.zero) {
        this.origin = origin
        this.size = size
    }

    constructor(x: Double, y: Double, width: Double, height: Double): this(origin = CGPoint(x = x, y = y), size = CGSize(width = width, height = height)) {
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as CGRect
        this.origin = copy.origin
        this.size = copy.size
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = CGRect(this as MutableStruct)

    override fun equals(other: Any?): Boolean {
        if (other !is CGRect) return false
        return origin == other.origin && size == other.size
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, origin)
        result = Hasher.combine(result, size)
        return result
    }

    companion object {
        val zero = CGRect()
    }
}
