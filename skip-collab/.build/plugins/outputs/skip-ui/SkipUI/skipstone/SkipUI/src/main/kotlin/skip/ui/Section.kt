// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.runtime.Composable

// Erase generics to facilitate specialized constructor support.
class Section: View, ListItemFactory {
    internal val header: View?
    internal val footer: View?
    internal val content: View

    constructor(content: () -> View, header: () -> View, footer: () -> View) {
        this.header = header()
        this.footer = footer()
        this.content = content()
    }

    constructor(content: () -> View, footer: () -> View, @Suppress("UNUSED_PARAMETER") unusedp_0: Nothing? = null) {
        this.header = null
        this.footer = footer()
        this.content = content()
    }

    constructor(content: () -> View, header: () -> View) {
        this.header = header()
        this.footer = null
        this.content = content()
    }

    constructor(content: () -> View) {
        this.header = null
        this.footer = null
        this.content = content()
    }

    constructor(titleKey: LocalizedStringKey, content: () -> View): this(titleKey.value, content = content) {
    }

    constructor(title: String, content: () -> View): this(content = content, header = {
        ComposeView { composectx: ComposeContext ->
            Text(title).Compose(composectx)
            ComposeResult.ok
        }
    }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, isExpanded: Binding<Boolean>, content: () -> View): this(titleKey, content = content) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, isExpanded: Binding<Boolean>, content: () -> View): this(title, content = content) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(isExpanded: Binding<Boolean>, content: () -> View, header: () -> View): this(content = content, header = header) {
    }

    @Composable
    override fun appendListItemViews(to: MutableList<View>, appendingContext: ComposeContext) {
        val views = to
        if (header != null) {
            views.add(ListSectionHeader(content = header))
        }
        content.Compose(context = appendingContext)
        if (footer != null) {
            views.add(ListSectionFooter(content = footer))
        }
    }

    override fun ComposeListItems(context: ListItemFactoryContext) {
        // Not called because the section does not append itself as a list item view
    }

    companion object {
    }
}
