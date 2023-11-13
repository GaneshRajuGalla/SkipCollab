// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.runtime.Composable

class Divider: View {
    constructor() {
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/Divider.kt
    @Composable
    fun Divider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
    )
    */
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val dividerColor = EnvironmentValues.shared._color?.colorImpl?.invoke()
        androidx.compose.material3.Divider(modifier = context.modifier, color = dividerColor ?: androidx.compose.ui.graphics.Color.LightGray)
    }

    companion object {
    }
}
