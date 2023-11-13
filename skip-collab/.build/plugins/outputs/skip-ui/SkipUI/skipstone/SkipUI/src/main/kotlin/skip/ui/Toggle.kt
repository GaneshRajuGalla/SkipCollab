// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.layout.Row
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Erase the generic Label to facilitate specialized constructor support.
//
class Toggle: View {
    internal val isOn: Binding<Boolean>
    internal val label: View

    constructor(isOn: Binding<Boolean>, label: () -> View) {
        this.isOn = isOn.sref()
        this.label = label()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(sources: Any, isOn: (Any) -> Binding<Boolean>, label: () -> View): this(isOn = isOn(0), label = label) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, sources: Any, isOn: (Any) -> Binding<Boolean>): this(isOn = isOn(0), label = {
        ComposeView { composectx: ComposeContext ->
            Text(title).Compose(composectx)
            ComposeResult.ok
        }
    }) {
    }

    constructor(title: String, isOn: Binding<Boolean>): this(isOn = isOn, label = {
        ComposeView { composectx: ComposeContext ->
            Text(title).Compose(composectx)
            ComposeResult.ok
        }
    }) {
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/Switch.kt
    @Composable
    @Suppress("ComposableLambdaParameterNaming", "ComposableLambdaParameterPosition")
    fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    thumbContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: SwitchColors = SwitchDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    )
    */
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val colors: SwitchColors
        val matchtarget_0 = EnvironmentValues.shared._tint
        if (matchtarget_0 != null) {
            val tint = matchtarget_0
            val tintColor = tint.colorImpl()
            colors = SwitchDefaults.colors(checkedTrackColor = tintColor, disabledCheckedTrackColor = tintColor.copy(alpha = ContentAlpha.disabled))
        } else {
            colors = SwitchDefaults.colors()
        }
        if (EnvironmentValues.shared._labelsHidden) {
            Switch(checked = isOn.wrappedValue, onCheckedChange = { it -> isOn.wrappedValue = it }, modifier = context.modifier)
        } else {
            val contentContext = context.content()
            ComposeContainer(modifier = context.modifier, fillWidth = true) { modifier ->
                Row(modifier = modifier, verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    label.Compose(context = contentContext)
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1.0f))
                    Switch(checked = isOn.wrappedValue, onCheckedChange = { it -> isOn.wrappedValue = it }, enabled = EnvironmentValues.shared.isEnabled, colors = colors)
                }
            }
        }
    }

    companion object {
    }
}

// Model `ToggleStyle` as a struct. Kotlin does not support static members of protocols
class ToggleStyle: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ToggleStyle) return false
        return rawValue == other.rawValue
    }

    companion object {

        val automatic = ButtonStyle(rawValue = 0)

        @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
        val button = ButtonStyle(rawValue = 1)

        val switch = ButtonStyle(rawValue = 2)
    }
}

