// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable

class ZStack<Content>: View where Content: View {
    internal val alignment: Alignment
    internal val content: Content

    constructor(alignment: Alignment = Alignment.center, content: () -> Content) {
        this.alignment = alignment.sref()
        this.content = content()
    }

    /*
    https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/foundation/foundation-layout/src/commonMain/kotlin/androidx/compose/foundation/layout/Box.kt
    @Composable
    inline fun Box(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit
    )
    */
    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val boxAlignment: androidx.compose.ui.Alignment
        when (alignment) {
            Alignment.leading, Alignment.leadingFirstTextBaseline, Alignment.leadingLastTextBaseline -> boxAlignment = androidx.compose.ui.Alignment.CenterStart.sref()
            Alignment.trailing, Alignment.trailingFirstTextBaseline, Alignment.trailingLastTextBaseline -> boxAlignment = androidx.compose.ui.Alignment.CenterEnd.sref()
            Alignment.top -> boxAlignment = androidx.compose.ui.Alignment.TopCenter.sref()
            Alignment.bottom -> boxAlignment = androidx.compose.ui.Alignment.BottomCenter.sref()
            Alignment.topLeading -> boxAlignment = androidx.compose.ui.Alignment.TopStart.sref()
            Alignment.topTrailing -> boxAlignment = androidx.compose.ui.Alignment.TopEnd.sref()
            Alignment.bottomLeading -> boxAlignment = androidx.compose.ui.Alignment.BottomStart.sref()
            Alignment.bottomTrailing -> boxAlignment = androidx.compose.ui.Alignment.BottomEnd.sref()
            else -> boxAlignment = androidx.compose.ui.Alignment.Center.sref()
        }
        val contentContext = context.content()
        ComposeContainer(modifier = context.modifier) { modifier ->
            Box(modifier = modifier, contentAlignment = boxAlignment) { content.Compose(context = contentContext) }
        }
    }

    companion object {
    }
}

