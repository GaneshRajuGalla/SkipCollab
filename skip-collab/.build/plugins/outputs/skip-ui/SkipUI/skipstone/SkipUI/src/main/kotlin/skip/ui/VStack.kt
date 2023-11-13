// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class VStack<Content>: View where Content: View {
    internal val alignment: HorizontalAlignment
    internal val spacing: Double?
    internal val content: Content

    constructor(alignment: HorizontalAlignment = HorizontalAlignment.center, spacing: Double? = null, content: () -> Content) {
        this.alignment = alignment
        this.spacing = spacing
        this.content = content()
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/foundation/foundation-layout/src/commonMain/kotlin/androidx/compose/foundation/layout/Column.kt
    @Composable
    inline fun Column(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
    )
    */
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val columnAlignment: androidx.compose.ui.Alignment.Horizontal
        when (alignment) {
            HorizontalAlignment.leading -> columnAlignment = androidx.compose.ui.Alignment.Start.sref()
            HorizontalAlignment.trailing -> columnAlignment = androidx.compose.ui.Alignment.End.sref()
            else -> columnAlignment = androidx.compose.ui.Alignment.CenterHorizontally.sref()
        }
        val contentContext: ComposeContext
        val columnArrangement: Arrangement.Vertical
        if (spacing != null) {
            contentContext = context.content()
            columnArrangement = Arrangement.spacedBy(spacing.dp)
        } else {
            contentContext = context.content(composer = VStackComposer())
            columnArrangement = Arrangement.spacedBy(0.dp)
        }
        ComposeContainer(modifier = context.modifier) { modifier ->
            Column(modifier = modifier, verticalArrangement = columnArrangement, horizontalAlignment = columnAlignment) {
                val fillHeightModifier = Modifier.weight(1.0f) // Only available in Column context
                EnvironmentValues.shared.setValues({ it -> it.set_fillHeightModifier(fillHeightModifier) }, in_ = { content.Compose(context = contentContext) })
            }
        }
    }

    companion object {
    }
}

internal open class VStackComposer: Composer {

    private var lastViewWasText: Boolean? = null

    override fun willCompose() {
        lastViewWasText = null
    }

    @Composable
    override fun Compose(view: View, context: (Boolean) -> ComposeContext) {
        // If the Text has spacing modifiers, no longer special case its spacing
        val isText = view.strippingModifiers(until = { it -> it == ComposeModifierRole.spacing }) { it -> it is Text }
        var contentContext = context(false)
        lastViewWasText?.let { lastViewWasText ->
            val spacing = if (lastViewWasText && isText) Companion.textSpacing else Companion.defaultSpacing
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(spacing.dp))
        }
        view.ComposeContent(context = contentContext)
        lastViewWasText = isText
    }

    companion object {
        private val defaultSpacing = 8.0
        // SwiftUI spaces adaptively based on font, etc, but this is at least closer to SwiftUI than our defaultSpacing
        private val textSpacing = 1.0
    }
}

