// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp

// Erase the SelectionValue because it is currently unused in Kotlin, the compiler won't be able to calculate it
//
class List<Content>: View where Content: View {
    internal val fixedContent: Content?
    internal val forEach: ForEach<Content>?

    internal constructor(fixedContent: Content? = null, indexRange: IntRange? = null, indexedContent: ((Int) -> Content)? = null, objects: RandomAccessCollection<Any>? = null, identifier: ((Any) -> AnyHashable)? = null, objectContent: ((Any) -> Content)? = null) {
        this.fixedContent = fixedContent.sref()
        if (indexRange != null) {
            this.forEach = ForEach(indexRange = indexRange, indexedContent = indexedContent)
        } else if (objects != null) {
            this.forEach = ForEach(objects = objects, identifier = identifier, objectContent = objectContent)
        } else {
            this.forEach = null
        }
    }

    constructor(content: () -> Content): this(fixedContent = content()) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(selection: Binding<Any>, content: () -> Content): this(fixedContent = content()) {
    }

    constructor(data: IntRange, rowContent: (Int) -> Content): this(indexRange = data, indexedContent = rowContent) {
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val style = EnvironmentValues.shared._listStyle ?: ListStyle.automatic
        val itemContext = context.content()
        ComposeContainer(modifier = context.modifier, fillWidth = true, fillHeight = true, then = Modifier.background(BackgroundColor(style = style))) { modifier ->
            Box(modifier = modifier) { ComposeList(context = itemContext, style = style) }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun ComposeList(context: ComposeContext, style: ListStyle) {
        var modifier: Modifier = Modifier
        if (style != ListStyle.plain) {
            modifier = modifier.padding(start = Companion.horizontalInset.dp, end = Companion.horizontalInset.dp)
        }
        modifier = modifier.fillWidth()

        // Collect all top-level views to compose. The LazyColumn itself is not a composable context, so we have to execute
        // our content's Compose function to collect its views before entering the LazyColumn body, then use LazyColumn's
        // LazyListScope functions to compose individual items
        val views: MutableList<View> = mutableListOf() // Use MutableList to avoid copies
        val viewsCollector = context.content(composer = ClosureComposer { view, context ->
            val matchtarget_0 = view as? ListItemFactory
            if (matchtarget_0 != null) {
                val factory = matchtarget_0
                factory.appendListItemViews(to = views, appendingContext = context(true))
            } else {
                views.add(view)
            }
        })
        if (forEach != null) {
            forEach.appendListItemViews(to = views, appendingContext = viewsCollector)
        } else if (fixedContent != null) {
            fixedContent.Compose(context = viewsCollector)
        }

        LazyColumn(modifier = modifier) {
            val itemContext = context.content(composer = ClosureComposer { view, context -> ComposeItem(view = view, context = context(false), style = style) })
            val sectionHeaderContext = context.content(composer = ClosureComposer { view, context -> ComposeSectionHeader(view = view, context = context(false), style = style, isTop = false) })
            val topSectionHeaderContext = context.content(composer = ClosureComposer { view, context -> ComposeSectionHeader(view = view, context = context(false), style = style, isTop = true) })
            val sectionFooterContext = context.content(composer = ClosureComposer { view, context -> ComposeSectionFooter(view = view, context = context(false), style = style) })
            var itemCount = 0
            val factoryContext = ListItemFactoryContext(item = { view ->
                item { view.Compose(context = itemContext) }
                itemCount += 1
            }, indexedItems = { range, factory ->
                val count = (range.endExclusive - range.start).sref()
                items(count) { index -> factory(index).Compose(context = itemContext) }
                itemCount += count
            }, objectItems = { objects, identifier, factory ->
                items(count = objects.count, key = { it -> identifier(objects[it]) }) { index -> factory(objects[index]).Compose(context = itemContext) }
                itemCount += objects.count
            }, sectionHeader = { view ->
                // Important to check the count immediately, outside the lazy list scope blocks
                val context = (if (itemCount == 0) topSectionHeaderContext else sectionHeaderContext).sref()
                if (style == ListStyle.plain) {
                    stickyHeader { view.Compose(context = context) }
                } else {
                    item { view.Compose(context = context) }
                }
                itemCount += 1
            }, sectionFooter = { view ->
                item { view.Compose(context = sectionFooterContext) }
                itemCount += 1
            })

            item { ComposeHeader(style = style) }
            for (view in views.sref()) {
                val matchtarget_1 = view as? ListItemFactory
                if (matchtarget_1 != null) {
                    val factory = matchtarget_1
                    factory.ComposeListItems(context = factoryContext)
                } else {
                    item { view.Compose(context = itemContext) }
                    itemCount += 1
                }
            }
            item { ComposeFooter(style = style) }
        }
    }

    @Composable
    private fun ComposeItem(view: View, context: ComposeContext, style: ListStyle) {
        val contentModifier = Modifier.padding(horizontal = Companion.horizontalItemInset.dp, vertical = Companion.verticalItemInset.dp).fillWidth().requiredHeightIn(min = Companion.minimumItemHeight.dp)
        Column(modifier = Modifier.background(BackgroundColor(style = ListStyle.plain)).then(context.modifier)) {
            // Note that we're calling the same view's Compose function again with a new context
            view.Compose(context = context.content(composer = ListItemComposer(contentModifier = contentModifier)))
            ComposeSeparator()
        }
    }

    @Composable
    private fun ComposeSeparator(): Unit = Box(modifier = Modifier.padding(start = Companion.horizontalItemInset.dp).fillWidth().height(1.dp).background(MaterialTheme.colorScheme.surfaceVariant))

    @Composable
    private fun ComposeSectionHeader(view: View, context: ComposeContext, style: ListStyle, isTop: Boolean) {
        if (!isTop) {
            ComposeFooter(style = style)
        }
        var contentModifier = Modifier.fillWidth()
        if (isTop && style != ListStyle.plain) {
            contentModifier = contentModifier.padding(start = Companion.horizontalItemInset.dp, top = 0.dp, end = Companion.horizontalItemInset.dp, bottom = Companion.verticalItemInset.dp)
        } else {
            contentModifier = contentModifier.padding(horizontal = Companion.horizontalItemInset.dp, vertical = Companion.verticalItemInset.dp)
        }
        Column(modifier = Modifier.background(BackgroundColor(style = ListStyle.automatic)).then(context.modifier)) {
            EnvironmentValues.shared.setValues({ it -> it.set_listSectionHeaderStyle(style) }, in_ = { view.Compose(context = context.content(modifier = contentModifier)) })
        }
    }

    @Composable
    private fun ComposeSectionFooter(view: View, context: ComposeContext, style: ListStyle) {
        if (style == ListStyle.plain) {
            ComposeItem(view = view, context = context, style = style)
        } else {
            val modifier = Modifier.offset(y = -1.dp)
                .zIndex(2.0f)
                .background(BackgroundColor(style = style))
                .then(context.modifier)
            val contentModifier = Modifier.fillWidth().padding(horizontal = Companion.horizontalItemInset.dp, vertical = Companion.verticalItemInset.dp)
            Column(modifier = modifier) {
                EnvironmentValues.shared.setValues({ it -> it.set_listSectionFooterStyle(style) }, in_ = { view.Compose(context = context.content(modifier = contentModifier)) })
            }
        }
    }

    @Composable
    private fun ComposeHeader(style: ListStyle) {
        if (style == ListStyle.plain) {
            return
        }
        val modifier = Modifier.background(BackgroundColor(style = style))
            .fillWidth()
            .height(Companion.verticalInset.dp)
        Box(modifier = modifier)
    }

    @Composable
    private fun ComposeFooter(style: ListStyle) {
        if (style == ListStyle.plain) {
            return
        }
        val modifier = Modifier.fillWidth()
            .height(Companion.verticalInset.dp)
            .offset(y = -1.dp)
            .zIndex(2.0f)
            .background(BackgroundColor(style = style))
        Box(modifier = modifier)
    }

    @Composable
    private fun BackgroundColor(style: ListStyle): androidx.compose.ui.graphics.Color {
        if (style == ListStyle.plain) {
            return MaterialTheme.colorScheme.surface.sref()
        } else {
            return Color.systemBackground.colorImpl()
        }
    }

    companion object {

        private val horizontalInset = 16.0
        private val verticalInset = 32.0
        private val minimumItemHeight = 44.0
        private val horizontalItemInset = 16.0
        private val verticalItemInset = 4.0
    }
}


// Kotlin does not support generic constructor parameters, so we have to model many List constructors as functions

//extension List {
//    public init<Data, RowContent>(_ data: Data, @ViewBuilder rowContent: @escaping (Data.Element) -> RowContent) where Content == ForEach<Data, Data.Element.ID, RowContent>, Data : RandomAccessCollection, RowContent : View, Data.Element : Identifiable
//}
fun <ObjectType, Content> List(data: RandomAccessCollection<ObjectType>, rowContent: (ObjectType) -> Content): List<Content> where ObjectType: Identifiable<Hashable>, Content: View {
    return List(objects = data as RandomAccessCollection<Any>, identifier = { it -> (it as ObjectType).id }, objectContent = { it -> rowContent(it as ObjectType) })
}

//extension List {
//    public init<Data, ID, RowContent>(_ data: Data, id: KeyPath<Data.Element, ID>, @ViewBuilder rowContent: @escaping (Data.Element) -> RowContent) where Content == ForEach<Data, ID, RowContent>, Data : RandomAccessCollection, ID : Hashable, RowContent : View
//}
fun <ObjectType, Content> List(data: RandomAccessCollection<ObjectType>, id: (ObjectType) -> AnyHashable, rowContent: (ObjectType) -> Content): List<Content> where ObjectType: Any, Content: View {
    return List(objects = data as RandomAccessCollection<Any>, identifier = { it -> id(it as ObjectType) }, objectContent = { it -> rowContent(it as ObjectType) })
}

/// Adopted by views that generate list items.
internal interface ListItemFactory {
    /// Append views and view factories representing list itemsto the given mutable list.
    ///
    /// - Parameter appendingContext: Pass this context to the `Compose` function of a `ComposableView` to append all its child views.
    @Composable
    fun appendListItemViews(to: MutableList<View>, appendingContext: ComposeContext)

    /// Use the given context to compose individual list items and ranges of items.
    fun ComposeListItems(context: ListItemFactoryContext)
}

/// Adopted by views that adapt when used as a list item.
internal interface ListItemAdapting {
    /// Whether to compose this view as a list item or to use the standard compose pipeline.
    @Composable
    fun shouldComposeListItem(): Boolean

    /// Compose this view as a list item.
    @Composable
    fun ComposeListItem(context: ComposeContext, contentModifier: Modifier)
}

class ListItemFactoryContext {
    internal val item: (View) -> Unit
    internal val indexedItems: (IntRange, (Int) -> View) -> Unit
    internal val objectItems: (RandomAccessCollection<Any>, (Any) -> AnyHashable, (Any) -> View) -> Unit

    internal val sectionHeader: (View) -> Unit
    internal val sectionFooter: (View) -> Unit

    internal constructor(item: (View) -> Unit, indexedItems: (IntRange, (Int) -> View) -> Unit, objectItems: (RandomAccessCollection<Any>, (Any) -> AnyHashable, (Any) -> View) -> Unit, sectionHeader: (View) -> Unit, sectionFooter: (View) -> Unit) {
        this.item = item
        this.indexedItems = indexedItems
        this.objectItems = objectItems
        this.sectionHeader = sectionHeader
        this.sectionFooter = sectionFooter
    }

    companion object {
    }
}

internal class ListItemComposer: Composer {
    internal val contentModifier: Modifier

    @Composable
    override fun Compose(view: View, context: (Boolean) -> ComposeContext) {
        val matchtarget_2 = view as? ListItemAdapting
        if (matchtarget_2 != null) {
            val listItemAdapting = matchtarget_2
            if (listItemAdapting.shouldComposeListItem()) {
                listItemAdapting.ComposeListItem(context = context(false), contentModifier = contentModifier)
            } else if (view is ComposeModifierView) {
                view.ComposeContent(context = context(true))
            } else {
                Box(modifier = contentModifier, contentAlignment = androidx.compose.ui.Alignment.CenterStart) { view.ComposeContent(context = context(false)) }
            }
        } else if (view is ComposeModifierView) {
            view.ComposeContent(context = context(true))
        } else {
            Box(modifier = contentModifier, contentAlignment = androidx.compose.ui.Alignment.CenterStart) { view.ComposeContent(context = context(false)) }
        }
    }

    constructor(contentModifier: Modifier) {
        this.contentModifier = contentModifier
    }
}

/// Add to list items to render a section header.
internal class ListSectionHeader: View, ListItemFactory {
    internal val content: View

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        content.Compose(context = context)
    }

    @Composable
    override fun appendListItemViews(to: MutableList<View>, appendingContext: ComposeContext) {
        val views = to
        views.add(this)
    }

    override fun ComposeListItems(context: ListItemFactoryContext): Unit = context.sectionHeader(content)

    constructor(content: View) {
        this.content = content.sref()
    }
}

/// Add to list items to render a section footer.
internal class ListSectionFooter: View, ListItemFactory {
    internal val content: View

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        content.Compose(context = context)
    }

    @Composable
    override fun appendListItemViews(to: MutableList<View>, appendingContext: ComposeContext) {
        val views = to
        views.add(this)
    }

    override fun ComposeListItems(context: ListItemFactoryContext): Unit = context.sectionFooter(content)

    constructor(content: View) {
        this.content = content.sref()
    }
}

// Model `ListStyle` as an enum. Kotlin does not support static members of protocols
enum class ListStyle(override val rawValue: Int, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): RawRepresentable<Int> {
    automatic(0),

    sidebar(1),

    insetGrouped(2),

    grouped(3),

    inset(4),

    plain(5);

    companion object {
    }
}

fun ListStyle(rawValue: Int): ListStyle? {
    return when (rawValue) {
        0 -> ListStyle.automatic
        1 -> ListStyle.sidebar
        2 -> ListStyle.insetGrouped
        3 -> ListStyle.grouped
        4 -> ListStyle.inset
        5 -> ListStyle.plain
        else -> null
    }
}

sealed class ListItemTint: Sendable {
    class FixedCase(val associated0: Color): ListItemTint() {
    }
    class PreferredCase(val associated0: Color): ListItemTint() {
    }
    class MonochromeCase: ListItemTint() {
    }

    companion object {
        fun fixed(associated0: Color): ListItemTint = FixedCase(associated0)
        fun preferred(associated0: Color): ListItemTint = PreferredCase(associated0)
        val monochrome: ListItemTint = MonochromeCase()
    }
}

sealed class ListSectionSpacing: Sendable {
    class DefaultCase: ListSectionSpacing() {
    }
    class CompactCase: ListSectionSpacing() {
    }
    class CustomCase(val associated0: Double): ListSectionSpacing() {
    }

    companion object {
        val default: ListSectionSpacing = DefaultCase()
        val compact: ListSectionSpacing = CompactCase()
        fun custom(associated0: Double): ListSectionSpacing = CustomCase(associated0)
    }
}

