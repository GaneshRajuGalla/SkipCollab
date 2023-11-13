// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.runtime.Composable

/// Recognized modifier roles.
enum class ComposeModifierRole {
    accessibility,
    gesture,
    spacing,
    unspecified;

    companion object {
    }
}

/// Used internally by modifiers to apply changes to the context supplied to modified views.
internal open class ComposeModifierView: View {
    internal val view: View
    internal val role: ComposeModifierRole
    internal open var contextTransform: (@Composable (InOut<ComposeContext>) -> Unit)? = null
    internal open var composeContent: (@Composable (View, ComposeContext) -> Unit)? = null

    /// A modfiier that transforms the `ComposeContext` passed to the modified view.
    internal constructor(contextView: View, role: ComposeModifierRole = ComposeModifierRole.unspecified, contextTransform: @Composable (InOut<ComposeContext>) -> Unit) {
        // Don't copy view
        this.view = contextView
        this.role = role
        this.contextTransform = contextTransform
        this.composeContent = null
    }

    /// A modifier that takes over the composition of the modified view.
    internal constructor(contentView: View, role: ComposeModifierRole = ComposeModifierRole.unspecified, composeContent: @Composable (View, ComposeContext) -> Unit) {
        // Don't copy view
        this.view = contentView
        this.role = role
        this.contextTransform = null
        this.composeContent = composeContent
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val matchtarget_0 = composeContent
        if (matchtarget_0 != null) {
            val composeContent = matchtarget_0
            composeContent(view, context)
        } else {
            contextTransform?.let { contextTransform ->
                var context = context.sref()
                contextTransform(InOut({ context }, { context = it }))
                view.ComposeContent(context)
            }
        }
    }

    override fun <R> strippingModifiers(until: (ComposeModifierRole) -> Boolean, perform: (View?) -> R): R {
        if (until(role)) {
            return perform(this)
        } else {
            return view.strippingModifiers(until = until, perform = perform)
        }
    }
}
