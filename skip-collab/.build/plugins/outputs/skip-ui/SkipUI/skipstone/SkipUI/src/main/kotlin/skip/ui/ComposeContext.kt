// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Modifier

/// Context to provide modifiers, parent, etc to composables.
class ComposeContext: MutableStruct {
    /// Modifiers to apply.
    var modifier: Modifier
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    /// Mechanism for a parent view to change how a child view is composed.
    var composer: Composer? = null
        get() = field.sref({ this.composer = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }

    /// Use in conjunction with `rememberSaveable` to store view state.
    var stateSaver: Saver<Any?, Any>
        get() = field.sref({ this.stateSaver = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }

    /// The context to pass to child content of a container view.
    ///
    /// By default, modifiers and the `composer` are reset for child content.
    fun content(modifier: Modifier = Modifier, composer: Composer? = null, stateSaver: Saver<Any?, Any>? = null): ComposeContext {
        var context = this.sref()
        context.modifier = modifier
        context.composer = composer
        context.stateSaver = stateSaver ?: this.stateSaver
        return context.sref()
    }

    constructor(modifier: Modifier = Modifier, composer: Composer? = null, stateSaver: Saver<Any?, Any> = ComposeStateSaver()) {
        this.modifier = modifier
        this.composer = composer
        this.stateSaver = stateSaver
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = ComposeContext(modifier, composer, stateSaver)

    companion object {
    }
}

/// The result of composing content.
///
/// Reserved for future use. Having a return value also expands recomposition scope. See `ComposeView` for details.
class ComposeResult {

    companion object {
        val ok = ComposeResult()
    }
}

/// Mechanism for a parent view to change how a child view is composed.
interface Composer {
    fun willCompose() = Unit

    fun didCompose(result: ComposeResult) = Unit

    /// Compose the given view.
    ///
    /// - Parameter context: The context to use to render the view, optionally retaining this composer.
    @Composable
    fun Compose(view: View, context: (Boolean) -> ComposeContext)
}

/// Builtin composer that executes a closure to compose.
///
/// - Warning: Child composables may recompose at any time. Be careful with relying on block capture.
internal class ClosureComposer: Composer {
    private val compose: @Composable (View, (Boolean) -> ComposeContext) -> Unit

    internal constructor(compose: @Composable (View, (Boolean) -> ComposeContext) -> Unit) {
        this.compose = compose
    }

    @Composable
    override fun Compose(view: View, context: (Boolean) -> ComposeContext): Unit = compose(view, context)
}

