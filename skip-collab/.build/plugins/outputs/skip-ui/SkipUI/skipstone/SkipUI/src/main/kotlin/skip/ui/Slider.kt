// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.material.ContentAlpha
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable

class Slider: View {
    internal val value: Binding<Double>
    internal val bounds: ClosedRange<Double>
    internal val step: Double?

    constructor(value: Binding<Double>, in_: ClosedRange<Double> = 0.0..1.0, step: Double? = null) {
        val bounds = in_
        this.value = value.sref()
        this.bounds = bounds
        this.step = step
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(value: Binding<Double>, in_: ClosedRange<Double> = 0.0..1.0, step: Double? = null, onEditingChanged: (Boolean) -> Unit) {
        val bounds = in_
        this.value = value.sref()
        this.bounds = bounds
        this.step = step
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(value: Binding<Double>, in_: ClosedRange<Double> = 0.0..1.0, step: Double? = null, label: () -> View, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
        val bounds = in_
        this.value = value.sref()
        this.bounds = bounds
        this.step = step
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(value: Binding<Double>, in_: ClosedRange<Double> = 0.0..1.0, step: Double? = null, label: () -> View, minimumValueLabel: () -> View, maximumValueLabel: () -> View, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
        val bounds = in_
        this.value = value.sref()
        this.bounds = bounds
        this.step = step
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/Slider.kt
    @Composable
    fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0)
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = SliderDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    )
    */
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        var steps = 0
        if ((step != null) && (step > 0.0)) {
            steps = Int(ceil(bounds.endInclusive - bounds.start) / step)
        }
        val colors: SliderColors
        val matchtarget_0 = EnvironmentValues.shared._tint
        if (matchtarget_0 != null) {
            val tint = matchtarget_0
            val activeColor = tint.colorImpl()
            val disabledColor = activeColor.copy(alpha = ContentAlpha.disabled)
            colors = SliderDefaults.colors(thumbColor = activeColor, activeTrackColor = activeColor, disabledThumbColor = disabledColor, disabledActiveTrackColor = disabledColor)
        } else {
            colors = SliderDefaults.colors()
        }
        androidx.compose.material3.Slider(value = Float(value.get()), onValueChange = { it -> value.set(Double(it)) }, modifier = context.modifier, enabled = EnvironmentValues.shared.isEnabled, valueRange = Float(bounds.start)..Float(bounds.endInclusive), steps = steps, colors = colors)
    }

    companion object {
    }
}
