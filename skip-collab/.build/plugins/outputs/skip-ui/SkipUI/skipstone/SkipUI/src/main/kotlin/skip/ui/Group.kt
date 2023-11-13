// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.runtime.Composable

class Group<Content>: View where Content: View {
    internal val content: Content

    constructor(content: () -> Content) {
        this.content = content()
    }

    @Composable
    override fun Compose(context: ComposeContext): ComposeResult {
        ComposeContent(context = context)
        return ComposeResult.ok
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        content.Compose(context = context)
    }

    companion object {
    }
}
