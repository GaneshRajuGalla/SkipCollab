// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Erase the generic Label to facilitate specialized constructor support.
class Button: View, ListItemAdapting {
    internal val action: () -> Unit
    internal val label: View

    constructor(action: () -> Unit, label: () -> View) {
        this.action = action
        this.label = label()
    }

    constructor(title: String, action: () -> Unit): this(action = action, label = {
        ComposeView { composectx: ComposeContext ->
            Text(title).Compose(composectx)
            ComposeResult.ok
        }
    }) {
    }

    constructor(titleKey: LocalizedStringKey, action: () -> Unit): this(titleKey.value, action = action) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(role: ButtonRole?, action: () -> Unit, label: () -> View): this(action = action, label = label) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, role: ButtonRole?, action: () -> Unit): this(title, action = action) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, role: ButtonRole?, action: () -> Unit): this(titleKey, action = action) {
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/Button.kt
    @Composable
    fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
    )
    */
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val buttonStyle = EnvironmentValues.shared._buttonStyle
        val contentContext = context.content()
        ComposeContainer(modifier = context.modifier) { modifier ->
            when (buttonStyle) {
                ButtonStyle.bordered -> {
                    val colors: ButtonColors
                    val matchtarget_0 = EnvironmentValues.shared._tint
                    if (matchtarget_0 != null) {
                        val tint = matchtarget_0
                        val tintColor = tint.colorImpl()
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = tintColor.copy(alpha = 0.15f), contentColor = tintColor, disabledContainerColor = tintColor.copy(alpha = 0.15f), disabledContentColor = tintColor.copy(alpha = ContentAlpha.medium))
                    } else {
                        colors = ButtonDefaults.filledTonalButtonColors()
                    }
                    FilledTonalButton(onClick = action, modifier = modifier, enabled = EnvironmentValues.shared.isEnabled, colors = colors) { label.Compose(context = contentContext) }
                }
                ButtonStyle.borderedProminent -> {
                    val colors: ButtonColors
                    val matchtarget_1 = EnvironmentValues.shared._tint
                    if (matchtarget_1 != null) {
                        val tint = matchtarget_1
                        val tintColor = tint.colorImpl()
                        colors = ButtonDefaults.buttonColors(containerColor = tintColor, disabledContainerColor = tintColor.copy(alpha = ContentAlpha.disabled))
                    } else {
                        colors = ButtonDefaults.buttonColors()
                    }
                    androidx.compose.material3.Button(onClick = action, modifier = modifier, enabled = EnvironmentValues.shared.isEnabled, colors = colors) { label.Compose(context = contentContext) }
                }
                else -> ComposePlain(context = context.content(modifier = modifier.clickable(onClick = action, enabled = EnvironmentValues.shared.isEnabled)))
            }
        }
    }

    @Composable
    private fun ComposePlain(context: ComposeContext) {
        val isEnabled = EnvironmentValues.shared.isEnabled
        val disabledAlpha = Double(ContentAlpha.disabled)
        EnvironmentValues.shared.setValues({ it ->
            if (it._color == null) {
                var buttonColor = it._tint ?: Color.accentColor
                if (!isEnabled) {
                    buttonColor = buttonColor.opacity(disabledAlpha)
                }
                it.set_color(buttonColor)
            }
        }, in_ = { label.Compose(context = context) })
    }

    @Composable
    override fun shouldComposeListItem(): Boolean {
        val buttonStyle = EnvironmentValues.shared._buttonStyle
        return buttonStyle == null || buttonStyle == ButtonStyle.automatic
    }

    @Composable
    override fun ComposeListItem(context: ComposeContext, contentModifier: Modifier) {
        Box(modifier = Modifier.clickable(onClick = action, enabled = EnvironmentValues.shared.isEnabled).then(contentModifier), contentAlignment = androidx.compose.ui.Alignment.CenterStart) { ComposePlain(context = context) }
    }

    companion object {
    }
}

// Model `ButtonStyle` as a struct. Kotlin does not support static members of protocols
class ButtonStyle: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ButtonStyle) return false
        return rawValue == other.rawValue
    }

    companion object {

        val automatic = ButtonStyle(rawValue = 0)
        val plain = ButtonStyle(rawValue = 1)
        val borderless = ButtonStyle(rawValue = 2)
        val bordered = ButtonStyle(rawValue = 3)
        val borderedProminent = ButtonStyle(rawValue = 4)
    }
}

enum class ButtonRepeatBehavior: Sendable {
    automatic,
    enabled,
    disabled;

    companion object {
    }
}

enum class ButtonRole: Sendable {
    destructive,
    cancel;

    companion object {
    }
}

