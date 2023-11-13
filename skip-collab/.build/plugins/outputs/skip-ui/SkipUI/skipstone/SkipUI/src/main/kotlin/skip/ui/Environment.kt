// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import kotlin.reflect.KClass
import skip.lib.*


import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import kotlin.reflect.full.companionObjectInstance

interface EnvironmentKey<Value> {
}

/// Added to `EnvironmentKey` companion objects.
interface EnvironmentKeyCompanion<Value> {
    val defaultValue: Value
}

// Model as a class because our implementation only holds the global environment keys, and so does not need to copy.
// Each key handles its own scoping of values using Android's `CompositionLocal` system
open class EnvironmentValues {

    // We type erase all keys and values. The alternative would be to reify these functions.
    internal val compositionLocals: MutableMap<Any, ProvidableCompositionLocal<Any>> = mutableMapOf()
    internal val lastSetValues: MutableMap<ProvidableCompositionLocal<Any>, Any> = mutableMapOf()

    /// Retrieve an environment value by its `EnvironmentKey`.
    @Composable operator fun <Key, Value> get(key: KClass<Key>): Value where Key: EnvironmentKey<Value> {
        val compositionLocal = valueCompositionLocal(key = key)
        return (compositionLocal.current as Value).sref()
    }

    /// Retrieve an environment object by type.
    @Composable fun <ObjectType> environmentObject(type: KClass<ObjectType>): ObjectType? where ObjectType: Any {
        val compositionLocal = objectCompositionLocal(type = type)
        val value = compositionLocal.current.sref()
        return (if (value == Unit) null else value as ObjectType).sref()
    }

    /// Set environment values.
    ///
    /// - Seealso: ``View/environment(_:)``
    /// - Warning: Setting environment values should only be done within the `execute` block of this function.
    @Composable
    internal open fun setValues(execute: @Composable (EnvironmentValues) -> Unit, in_: @Composable () -> Unit) {
        val content = in_
        // Set the values in EnvironmentValues to keep any user-defined setter logic in place, then retrieve and clear the last set values
        execute(this)
        val provided = lastSetValues.map { entry ->
            val element = entry.key provides entry.value
            element
        }.toTypedArray()
        lastSetValues.clear()
        CompositionLocalProvider(*provided) { content() }
    }

    // On set we populate our `lastSetValues` map, which our `setValues` function reads from and then clears after
    // packaging the values for sending to downstream Composables. This should be safe to do even on this effectively
    // global object because it should only be occurring sequentially on the main thread.

    operator fun <Key, Value> set(key: KClass<Key>, value: Value) where Key: EnvironmentKey<Value>, Value: Any {
        val compositionLocal = valueCompositionLocal(key = key)
        lastSetValues[compositionLocal] = value.sref()
    }

    /// The Compose `CompositionLocal` for the given environment value key type.
    open fun valueCompositionLocal(key: KClass<*>): ProvidableCompositionLocal<Any> {
        val defaultValue = { (key.companionObjectInstance as EnvironmentKeyCompanion<*>).defaultValue }
        return compositionLocal(key = key, defaultValue = defaultValue)
    }

    /// The Compose `CompositionLocal` for the given environment object type.
    open fun objectCompositionLocal(type: KClass<*>): ProvidableCompositionLocal<Any> {
        return compositionLocal(key = type, defaultValue = { null })
    }

    internal open fun compositionLocal(key: AnyHashable, defaultValue: () -> Any?): ProvidableCompositionLocal<Any> {
        compositionLocals[key].sref()?.let { value ->
            return value.sref()
        }
        val value = compositionLocalOf { defaultValue() ?: Unit }
        compositionLocals[key] = value.sref()
        return value.sref()
    }

    @Composable
    private fun builtinValue(key: AnyHashable, defaultValue: () -> Any?): Any? {
        val compositionLocal = compositionLocal(key = key, defaultValue = defaultValue)
        val current = compositionLocal.current.sref()
        return (if (current == Unit) null else current).sref()
    }

    private fun setBuiltinValue(key: AnyHashable, value: Any?, defaultValue: () -> Any?) {
        val compositionLocal = compositionLocal(key = key, defaultValue = defaultValue)
        lastSetValues[compositionLocal] = (value ?: Unit).sref()
    }

    // MARK: - SwiftUI values

    open val dismiss: () -> Unit
        @Composable
        get() {
            return builtinValue(key = "dismiss", defaultValue = {
                {  }
            }) as () -> Unit
        }
    fun setdismiss(newValue: () -> Unit) {
        setBuiltinValue(key = "dismiss", value = newValue, defaultValue = {
            {  }
        })
    }

    open val font: Font?
        @Composable
        get() {
            return builtinValue(key = "font", defaultValue = { null }) as Font?
        }
    fun setfont(newValue: Font?) {
        setBuiltinValue(key = "font", value = newValue, defaultValue = { null })
    }

    open val isEnabled: Boolean
        @Composable
        get() {
            return builtinValue(key = "isEnabled", defaultValue = { true }) as Boolean
        }
    fun setisEnabled(newValue: Boolean) {
        setBuiltinValue(key = "isEnabled", value = newValue, defaultValue = { true })
    }

    open val lineLimit: Int?
        @Composable
        get() {
            return builtinValue(key = "lineLimit", defaultValue = { null }) as Int?
        }
    fun setlineLimit(newValue: Int?) {
        setBuiltinValue(key = "lineLimit", value = newValue, defaultValue = { null })
    }

    // MARK: - Internal values

    internal open val _aspectRatio: Tuple2<Double?, ContentMode>?
        @Composable
        get() {
            return builtinValue(key = "_aspectRatio", defaultValue = { null }) as Tuple2<Double?, ContentMode>?
        }
    internal fun set_aspectRatio(newValue: Tuple2<Double?, ContentMode>?) {
        setBuiltinValue(key = "_aspectRatio", value = newValue, defaultValue = { null })
    }

    internal open val _buttonStyle: ButtonStyle?
        @Composable
        get() {
            return builtinValue(key = "_buttonStyle", defaultValue = { null }) as ButtonStyle?
        }
    internal fun set_buttonStyle(newValue: ButtonStyle?) {
        setBuiltinValue(key = "_buttonStyle", value = newValue, defaultValue = { null })
    }

    internal open val _color: Color?
        @Composable
        get() {
            return builtinValue(key = "_color", defaultValue = { null }) as Color?
        }
    internal fun set_color(newValue: Color?) {
        setBuiltinValue(key = "_color", value = newValue, defaultValue = { null })
    }

    internal open val _fillHeight: (@Composable (Boolean) -> Modifier)?
        @Composable
        get() {
            return builtinValue(key = "_fillHeight", defaultValue = { null }) as (@Composable (Boolean) -> Modifier)?
        }
    internal fun set_fillHeight(newValue: (@Composable (Boolean) -> Modifier)?) {
        setBuiltinValue(key = "_fillHeight", value = newValue, defaultValue = { null })
    }

    internal open val _fillWidth: (@Composable (Boolean) -> Modifier)?
        @Composable
        get() {
            return builtinValue(key = "_fillWidth", defaultValue = { null }) as (@Composable (Boolean) -> Modifier)?
        }
    internal fun set_fillWidth(newValue: (@Composable (Boolean) -> Modifier)?) {
        setBuiltinValue(key = "_fillWidth", value = newValue, defaultValue = { null })
    }

    internal open val _fillHeightModifier: Modifier?
        @Composable
        get() {
            return builtinValue(key = "_fillHeightModifier", defaultValue = { null }) as Modifier?
        }
    internal fun set_fillHeightModifier(newValue: Modifier?) {
        setBuiltinValue(key = "_fillHeightModifier", value = newValue, defaultValue = { null })
    }

    internal open val _fillWidthModifier: Modifier?
        @Composable
        get() {
            return builtinValue(key = "_fillWidthModifier", defaultValue = { null }) as Modifier?
        }
    internal fun set_fillWidthModifier(newValue: Modifier?) {
        setBuiltinValue(key = "_fillWidthModifier", value = newValue, defaultValue = { null })
    }

    internal open val _fontWeight: Font.Weight?
        @Composable
        get() {
            return builtinValue(key = "_fontWeight", defaultValue = { null }) as Font.Weight?
        }
    internal fun set_fontWeight(newValue: Font.Weight?) {
        setBuiltinValue(key = "_fontWeight", value = newValue, defaultValue = { null })
    }

    internal open val _isItalic: Boolean
        @Composable
        get() {
            return builtinValue(key = "_isItalic", defaultValue = { false }) as Boolean
        }
    internal fun set_isItalic(newValue: Boolean) {
        setBuiltinValue(key = "_isItalic", value = newValue, defaultValue = { false })
    }

    internal open val _labelsHidden: Boolean
        @Composable
        get() {
            return builtinValue(key = "_labelsHidden", defaultValue = { false }) as Boolean
        }
    internal fun set_labelsHidden(newValue: Boolean) {
        setBuiltinValue(key = "_labelsHidden", value = newValue, defaultValue = { false })
    }

    internal open val _listItemTint: Color?
        @Composable
        get() {
            return builtinValue(key = "_listItemTint", defaultValue = { null }) as Color?
        }
    internal fun set_listItemTint(newValue: Color?) {
        setBuiltinValue(key = "_listItemTint", value = newValue, defaultValue = { null })
    }

    internal open val _listSectionHeaderStyle: ListStyle?
        @Composable
        get() {
            return builtinValue(key = "_listSectionHeaderStyle", defaultValue = { null }) as ListStyle?
        }
    internal fun set_listSectionHeaderStyle(newValue: ListStyle?) {
        setBuiltinValue(key = "_listSectionHeaderStyle", value = newValue, defaultValue = { null })
    }

    internal open val _listSectionFooterStyle: ListStyle?
        @Composable
        get() {
            return builtinValue(key = "_listSectionFooterStyle", defaultValue = { null }) as ListStyle?
        }
    internal fun set_listSectionFooterStyle(newValue: ListStyle?) {
        setBuiltinValue(key = "_listSectionFooterStyle", value = newValue, defaultValue = { null })
    }

    internal open val _listStyle: ListStyle?
        @Composable
        get() {
            return builtinValue(key = "_listStyle", defaultValue = { null }) as ListStyle?
        }
    internal fun set_listStyle(newValue: ListStyle?) {
        setBuiltinValue(key = "_listStyle", value = newValue, defaultValue = { null })
    }

    internal open val _progressViewStyle: ProgressViewStyle?
        @Composable
        get() {
            return builtinValue(key = "_progressViewStyle", defaultValue = { null }) as ProgressViewStyle?
        }
    internal fun set_progressViewStyle(newValue: ProgressViewStyle?) {
        setBuiltinValue(key = "_progressViewStyle", value = newValue, defaultValue = { null })
    }

    internal open val _sheetDepth: Int
        @Composable
        get() {
            return builtinValue(key = "_sheetDepth", defaultValue = { 0 }) as Int
        }
    internal fun set_sheetDepth(newValue: Int) {
        setBuiltinValue(key = "_sheetDepth", value = newValue, defaultValue = { 0 })
    }

    internal open val _tint: Color?
        @Composable
        get() {
            return builtinValue(key = "_tint", defaultValue = { null }) as Color?
        }
    internal fun set_tint(newValue: Color?) {
        setBuiltinValue(key = "_tint", value = newValue, defaultValue = { null })
    }

    companion object {
        val shared = EnvironmentValues()
    }
}



