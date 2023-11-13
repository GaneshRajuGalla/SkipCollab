// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.material.ContentAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable

// Erase the generic Label to facilitate specialized constructor support.
//
class TextField: View {
    internal val text: Binding<String>
    internal val label: View
    internal val prompt: Text?

    constructor(text: Binding<String>, prompt: Text? = null, label: () -> View) {
        this.text = text.sref()
        this.label = label()
        this.prompt = prompt
    }

    constructor(title: String, text: Binding<String>, prompt: Text? = null): this(text = text, prompt = prompt, label = {
        ComposeView { composectx: ComposeContext ->
            Text(title).Compose(composectx)
            ComposeResult.ok
        }
    }) {
    }

    constructor(titleKey: LocalizedStringKey, text: Binding<String>, prompt: Text? = null): this(text = text, prompt = prompt, label = {
        ComposeView { composectx: ComposeContext ->
            Text(titleKey.value).Compose(composectx)
            ComposeResult.ok
        }
    }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, text: Binding<String>, axis: Axis): this(titleKey, text = text) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, text: Binding<String>, prompt: Text?, axis: Axis): this(titleKey, text = text, prompt = prompt) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, text: Binding<String>, axis: Axis): this(title, text = text) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, text: Binding<String>, prompt: Text?, axis: Axis): this(title, text = text, prompt = prompt) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(text: Binding<String>, prompt: Text? = null, axis: Axis, label: () -> View): this(text = text, prompt = prompt, label = label) {
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/TextField.kt
    @Composable
    fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors()
    )
    */
    @ExperimentalMaterial3Api
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val contentContext = context.content()
        val textColor = (EnvironmentValues.shared._color ?: Color.primary).colorImpl()
        val colors: TextFieldColors
        val matchtarget_0 = EnvironmentValues.shared._tint
        if (matchtarget_0 != null) {
            val tint = matchtarget_0
            val tintColor = tint.colorImpl()
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = textColor, unfocusedTextColor = textColor, disabledTextColor = textColor.copy(alpha = ContentAlpha.disabled), cursorColor = tintColor, focusedBorderColor = tintColor)
        } else {
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedTextColor = textColor, unfocusedTextColor = textColor, disabledTextColor = textColor.copy(alpha = ContentAlpha.disabled))
        }
        OutlinedTextField(value = text.wrappedValue, onValueChange = { it -> text.wrappedValue = it }, modifier = context.modifier.fillWidth(), enabled = EnvironmentValues.shared.isEnabled, placeholder = { Placeholder(context = contentContext) }, singleLine = true, colors = colors)
    }

    @Composable
    private fun Placeholder(context: ComposeContext) {
        EnvironmentValues.shared.setValues({ it ->
            it.set_color(Color(colorImpl = { Color.primary.colorImpl().copy(alpha = ContentAlpha.disabled) }))
        }, in_ = {
            if (prompt != null) {
                prompt.Compose(context = context)
            } else {
                label.Compose(context = context)
            }
        })
    }

    companion object {
    }
}

// Model `TextFieldStyle` as an enum. Kotlin does not support static members of protocols
enum class TextFieldStyle(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): RawRepresentable<Int> {
    automatic(0),
    roundedBorder(1),

    plain(2);

    companion object {
    }
}

fun TextFieldStyle(rawValue: Int): TextFieldStyle? {
    return when (rawValue) {
        0 -> TextFieldStyle.automatic
        1 -> TextFieldStyle.roundedBorder
        2 -> TextFieldStyle.plain
        else -> null
    }
}

