// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

class Binding<Value> {
    internal val get: () -> Value
    internal val set: (Value) -> Unit

    constructor(get: () -> Value, set: (Value) -> Unit) {
        this.get = get
        this.set = set
    }


    constructor(projectedValue: Binding<Value>) {
        this.get = projectedValue.get.sref()
        this.set = projectedValue.set.sref()
    }

    var wrappedValue: Value
        get() = get().sref({ this.wrappedValue = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            set(newValue)
        }

    val projectedValue: Binding<Value>
        get() = this

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    val transaction: Any
        get() {
            fatalError()
        }

    companion object {
        /// Used to implement @Bindable.
        fun <Value, ObjectType> instance(object_: ObjectType, get: (ObjectType) -> Value, set: (ObjectType, Value) -> Unit): Binding<Value> {
            val capturedObject = object_.sref()
            return Binding(get = { get(capturedObject) }, set = { value -> set(capturedObject, value) })
        }

        fun <Value> constant(value: Value): Binding<Value> {
            return Binding(get = { value }, set = { _ ->  })
        }
    }
}

