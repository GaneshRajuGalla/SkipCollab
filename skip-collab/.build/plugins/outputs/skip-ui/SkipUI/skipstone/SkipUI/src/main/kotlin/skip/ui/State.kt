// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

// Model State as a class rather than struct to avoid copy overhead on mutation
class State<Value> {
    private var onUpdate: ((Value) -> Unit)? = null

    constructor(initialValue: Value, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        suppresssideeffects = true
        try {
            wrappedValue = initialValue
        } finally {
            suppresssideeffects = false
        }
    }

    constructor(wrappedValue: Value) {
        suppresssideeffects = true
        try {
            this.wrappedValue = wrappedValue
        } finally {
            suppresssideeffects = false
        }
    }

    var wrappedValue: Value
        get() = field.sref({ this.wrappedValue = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            field = newValue
            if (!suppresssideeffects) {
                onUpdate?.invoke(wrappedValue)
            }
        }

    val projectedValue: Binding<Value>
        get() {
            return Binding(get = { this.wrappedValue }, set = { it -> this.wrappedValue = it })
        }

    /// Used to keep the state value synchronized with an external Compose value.
    fun sync(value: Value, onUpdate: (Value) -> Unit) {
        this.wrappedValue = value
        this.onUpdate = onUpdate
    }

    private var suppresssideeffects = false

    companion object {
    }
}

// extension State where Value : ExpressibleByNilLiteral {
// public init() {
@Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
fun State() {
    fatalError()
}
// }
