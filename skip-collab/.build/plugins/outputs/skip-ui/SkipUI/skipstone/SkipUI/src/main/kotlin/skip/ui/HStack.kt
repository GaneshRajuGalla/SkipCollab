// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class HStack<Content>: View where Content: View {
    internal val alignment: VerticalAlignment
    internal val spacing: Double?
    internal val content: Content

    constructor(alignment: VerticalAlignment = VerticalAlignment.center, spacing: Double? = null, content: () -> Content) {
        this.alignment = alignment
        this.spacing = spacing
        this.content = content()
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/foundation/foundation-layout/src/commonMain/kotlin/androidx/compose/foundation/layout/Row.kt
    @Composable
    inline fun Row(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit
    )
    */
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val rowAlignment: androidx.compose.ui.Alignment.Vertical
        when (alignment) {
            VerticalAlignment.bottom -> rowAlignment = androidx.compose.ui.Alignment.Bottom.sref()
            VerticalAlignment.top -> rowAlignment = androidx.compose.ui.Alignment.Top.sref()
            else -> rowAlignment = androidx.compose.ui.Alignment.CenterVertically.sref()
        }
        val contentContext = context.content()
        ComposeContainer(modifier = context.modifier) { modifier ->
            Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy((spacing ?: 8.0).dp), verticalAlignment = rowAlignment) {
                val fillWidthModifier = Modifier.weight(1.0f) // Only available in Row context
                EnvironmentValues.shared.setValues({ it -> it.set_fillWidthModifier(fillWidthModifier) }, in_ = { content.Compose(context = contentContext) })
            }
        }
    }

    companion object {
    }
}

