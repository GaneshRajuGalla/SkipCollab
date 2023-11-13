// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import skip.foundation.*

class AppStorage<Value> {
    val key: String
    val store: UserDefaults?
    private var onUpdate: ((Value) -> Unit)? = null
    /// The property change listener from the UserDefaults
    private var listener: Any? = null

    constructor(wrappedValue: Value, key: String, store: UserDefaults? = null) {
        suppresssideeffects = true
        try {
            this.key = key
            this.store = store
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

    /// The current active store
    private val currentStore: UserDefaults
        get() {
            // TODO: handle Scene.defaultAppStorage() and View.defaultAppStorage() by storing it in the environment
            return store ?: UserDefaults.standard
        }

    /// Used to keep the state value synchronized with an external Compose value.
    fun sync(value: Value, onUpdate: (Value) -> Unit) {
        val initialValue = value
        val store = this.currentStore

        fun currentValue(): Value = (store.object_(forKey = key) as? Value ?: initialValue).sref()

        this.wrappedValue = currentValue()
        onUpdate(currentValue())

        // Caution: The preference manager does not currently store a strong reference to the listener. You must store a strong reference to the listener, or it will be susceptible to garbage collection. We recommend you keep a reference to the listener in the instance data of an object that will exist as long as you need the listener.
        // https://developer.android.com/reference/android/content/SharedPreferences.html#registerOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener)
        this.listener = store.registerOnSharedPreferenceChangeListener(key = key) { onUpdate(currentValue()) }

        this.onUpdate = { value ->
            onUpdate(value)
            currentStore.set(value, forKey = key)
        }
    }

    private var suppresssideeffects = false

    companion object {
    }
}


