// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Erase the generic Labels to facilitate specialized constructor support.
class ProgressView : View {
    internal val value: Double?
    internal val total: Double?

    constructor() {
        this.value = null
        this.total = null
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(label: () -> View) {
        this.value = null
        this.total = null
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey) {
        this.value = null
        this.total = null
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String) {
        this.value = null
        this.total = null
    }

    constructor(value: Double?, total: Double = 1.0) {
        this.value = value
        this.total = total
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(value: Double?, total: Double = 1.0, label: () -> View) {
        this.value = value
        this.total = total
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, value: Double?, total: Double = 1.0) {
        this.value = value
        this.total = total
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, value: Double?, total: Double = 1.0) {
        this.value = value
        this.total = total
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/ProgressIndicator.kt
    @Composable
    fun Circular/LinearProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.circular/linearColor
    trackColor: Color = ProgressIndicatorDefaults.circular/linearTrackColor
    strokeCap: StrokeCap = ProgressIndicatorDefaults.circular/Circular/LinearStrokeCap
    )
    */
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        var style = EnvironmentValues.shared._progressViewStyle ?: ProgressViewStyle.automatic
        if (style == ProgressViewStyle.automatic) {
            style = if (value == null) ProgressViewStyle.circular else ProgressViewStyle.linear
        }
        if (style == ProgressViewStyle.linear) {
            val color = (EnvironmentValues.shared._tint?.colorImpl?.invoke() ?: ProgressIndicatorDefaults.linearColor).sref()
            if (value == null || total == null) {
                LinearProgressIndicator(modifier = context.modifier, color = color)
            } else {
                LinearProgressIndicator(progress = Float(value!! / total!!), modifier = context.modifier, color = color)
            }
        } else {
            val color = (EnvironmentValues.shared._tint?.colorImpl?.invoke() ?: ProgressIndicatorDefaults.circularColor).sref()
            // Reduce size to better match SwiftUI
            val indicatorModifier = Modifier.size(20.dp)
            Box(modifier = context.modifier, contentAlignment = androidx.compose.ui.Alignment.Center) {
                if (value == null || total == null) {
                    CircularProgressIndicator(modifier = indicatorModifier, color = color)
                } else {
                    CircularProgressIndicator(progress = Float(value!! / total!!), modifier = indicatorModifier, color = color)
                }
            }
        }
    }

    companion object {
    }
}

// Model `ProgressViewStyle` as a struct. Kotlin does not support static members of protocols
class ProgressViewStyle: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ProgressViewStyle) return false
        return rawValue == other.rawValue
    }

    companion object {

        val automatic = ProgressViewStyle(rawValue = 0)
        val linear = ProgressViewStyle(rawValue = 1)
        val circular = ProgressViewStyle(rawValue = 2)
    }
}

