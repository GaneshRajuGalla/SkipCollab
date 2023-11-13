// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*


class LocalizedStringKey: MutableStruct {
    var value: String
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    constructor(value: String, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        this.value = value
    }

    constructor(stringLiteral: String) {
        val value = stringLiteral
        this.value = value
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as LocalizedStringKey
        this.value = copy.value
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = LocalizedStringKey(this as MutableStruct)

    override fun equals(other: Any?): Boolean {
        if (other !is LocalizedStringKey) return false
        return value == other.value
    }

    companion object {
    }
}

