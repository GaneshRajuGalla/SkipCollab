// Copyright 2023 Skip
//
// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.foundation

import skip.lib.*
import skip.lib.Array

// Note: !SKIP code paths used to validate implementation only.
// Not used in applications. See contribution guide for details.
typealias PlatformIndexPath = skip.lib.Array<Int>

class IndexPath: MutableStruct {

    internal var platformValue: skip.lib.Array<Int>
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    internal constructor(platformValue: skip.lib.Array<Int>) {
        this.platformValue = platformValue
    }

    constructor(index: Int) {
        this.platformValue = arrayOf(index)
    }

    val description: String
        get() = platformValue.description

    //    public typealias Index = Array<Int>.Index
    //    public typealias Indices = DefaultIndices<IndexPath>
    //    public init<ElementSequence>(indexes: ElementSequence) where ElementSequence : Sequence, ElementSequence.Element == Int
    //    public init(arrayLiteral indexes: IndexPath.Element...)
    //    public init(indexes: [IndexPath.Element])
    //    public init(index: IndexPath.Element)
    //    public func dropLast() -> IndexPath
    //    public mutating func append(_ other: IndexPath)
    //    public mutating func append(_ other: IndexPath.Element)
    //    public mutating func append(_ other: [IndexPath.Element])
    //    public func appending(_ other: IndexPath.Element) -> IndexPath
    //    public func appending(_ other: IndexPath) -> IndexPath
    //    public func appending(_ other: [IndexPath.Element]) -> IndexPath
    //    public subscript(index: IndexPath.Index) -> IndexPath.Element
    //    public subscript(range: Range<IndexPath.Index>) -> IndexPath
    //    public func makeIterator() -> IndexingIterator<IndexPath>
    //    public var count: Int { get }
    //    public var startIndex: IndexPath.Index { get }
    //    public var endIndex: IndexPath.Index { get }
    //    public func index(before i: IndexPath.Index) -> IndexPath.Index
    //    public func index(after i: IndexPath.Index) -> IndexPath.Index
    //    public func compare(_ other: IndexPath) -> ComparisonResult
    //    public func hash(into hasher: inout Hasher)
    //    public static func == (lhs: IndexPath, rhs: IndexPath) -> Bool
    //    public static func + (lhs: IndexPath, rhs: IndexPath) -> IndexPath
    //    public static func += (lhs: inout IndexPath, rhs: IndexPath)
    //    public static func < (lhs: IndexPath, rhs: IndexPath) -> Bool
    //    public static func <= (lhs: IndexPath, rhs: IndexPath) -> Bool
    //    public static func > (lhs: IndexPath, rhs: IndexPath) -> Bool
    //    public static func >= (lhs: IndexPath, rhs: IndexPath) -> Bool
    //    public typealias ArrayLiteralElement = IndexPath.Element
    //    public typealias Iterator = IndexingIterator<IndexPath>
    //    public typealias SubSequence = IndexPath
    //    public var hashValue: Int { get }

    fun kotlin(nocopy: Boolean = false): skip.lib.Array<Int> = if (nocopy) platformValue else platformValue.sref()

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as IndexPath
        this.platformValue = copy.platformValue
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = IndexPath(this as MutableStruct)

    override fun toString(): String = description

    override fun equals(other: Any?): Boolean {
        if (other !is IndexPath) return false
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

