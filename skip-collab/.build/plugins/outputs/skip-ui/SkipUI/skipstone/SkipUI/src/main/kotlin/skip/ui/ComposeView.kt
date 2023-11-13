// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.runtime.Composable

/// View that wraps `Composable` content.
///
/// Used to wrap the content of SwiftUI `@ViewBuilders`, and may be used manually to embed raw Compose code.
class ComposeView: View {
    private val content: @Composable (ComposeContext) -> ComposeResult

    /// Constructor.
    ///
    /// The supplied `content` is the content to compose. When transpiling SwiftUI code, this is the logic embedded in the user's `body` and within each container view in
    /// that `body`, as well as within other `@ViewBuilders`.
    ///
    /// - Note: Returning a result from `content` is important. This prevents Compose from recomposing `content` on its own. Instead, a change that would recompose
    ///   `content` elevates to our void `ComposeContent` function. This allows us to prepare for recompositions, e.g. making the proper callbacks to the context's `composer`.
    constructor(content: @Composable (ComposeContext) -> ComposeResult) {
        this.content = content
    }

    @Composable
    override fun Compose(context: ComposeContext): ComposeResult {
        ComposeContent(context)
        return ComposeResult.ok
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        context.composer?.willCompose()
        val result = content(context)
        context.composer?.didCompose(result = result)
    }

    companion object {
    }
}
