// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import kotlin.reflect.KClass
import skip.lib.*
import skip.lib.Array

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import kotlin.reflect.full.companionObjectInstance

interface PreferenceKey<Value> {
}

/// Added to `PreferenceKey` companion objects.
interface PreferenceKeyCompanion<Value> {
    val defaultValue: Value
    fun reduce(value: InOut<Value>, nextValue: () -> Value)
}

/// Internal analog to `EnvironmentValues` for preferences.
///
/// Uses environment `CompositionLocals` internally.
///
/// - Seealso: `EnvironmentValues`
internal open class PreferenceValues {

    /// Return a preference for the given `PreferenceKey` type.
    @Composable internal fun preference(key: KClass<*>): Preference<*>? = EnvironmentValues.shared.compositionLocals[key]?.current as? Preference<*>

    /// Collect the values of the given preferences while composing the given content.
    @Composable internal fun collectPreferences(preferences: Array<Preference<*>>, in_: @Composable () -> Unit) {
        val content = in_
        val provided = preferences.map { preference ->
            var compositionLocal = EnvironmentValues.shared.compositionLocals[preference.key].sref()
            if (compositionLocal == null) {
                compositionLocal = compositionLocalOf { Unit }
                EnvironmentValues.shared.compositionLocals[preference.key] = compositionLocal.sref()
            }
            val element = compositionLocal!! provides preference
            element
        }.kotlin(nocopy = true).toTypedArray()

        preferences.forEach { it -> it.beginCollecting() }
        CompositionLocalProvider(*provided) { content() }
        preferences.forEach { it -> it.endCollecting() }
    }

    companion object {
        internal val shared = PreferenceValues()
    }
}

/// Used internally by our preferences system to collect preferences and recompose on change.
internal open class Preference<Value> {
    internal val key: KClass<*>
    private val update: (Value) -> Unit
    private val didChange: () -> Unit
    private val initialValue: Value
    private var isCollecting = false
    private var collectedValue: Value? = null
        get() = field.sref({ this.collectedValue = it })
        set(newValue) {
            field = newValue.sref()
        }

    /// Create a preference for the given `PreferenceKey` type.
    ///
    /// - Parameter update: Block to call to change the value of this preference.
    /// - Parameter didChange: Block to call if this preference changes. Should force a recompose of the relevant content, collecting the new value via `collectPreferences`
    internal constructor(key: KClass<*>, initialValue: Value? = null, update: (Value) -> Unit, didChange: () -> Unit) {
        this.key = key
        this.update = update
        this.didChange = didChange
        this.initialValue = (initialValue ?: (key.companionObjectInstance as PreferenceKeyCompanion<Value>).defaultValue).sref()
    }

    /// The current preference value.
    internal open val value: Value
        get() = collectedValue ?: initialValue

    /// Reduce the current value and the given values.
    internal open fun reduce(savedValue: Any?, newValue: Any) {
        if (isCollecting) {
            var value = this.value.sref()
            (key.companionObjectInstance as PreferenceKeyCompanion<Value>).reduce(value = InOut({ value }, { value = it }), nextValue = { newValue as Value })
            collectedValue = value
        } else if (savedValue != newValue) {
            didChange()
        }
    }

    /// Begin collecting the current value.
    ///
    /// Call this before composing content.
    internal open fun beginCollecting() {
        isCollecting = true
        collectedValue = null
    }

    /// End collecting the current value.
    ///
    /// Call this after composing content.
    internal open fun endCollecting() {
        isCollecting = false
        update(value)
    }
}

