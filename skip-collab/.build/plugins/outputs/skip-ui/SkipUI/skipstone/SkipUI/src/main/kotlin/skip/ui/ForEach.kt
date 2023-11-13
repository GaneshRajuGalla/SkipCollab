// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

import androidx.compose.runtime.Composable

// Erase the Data and ID because they are currently unused in Kotlin, the compiler won't be able to calculate them
class ForEach<Content>: View, ListItemFactory where Content: View {
    internal val indexedContent: ((Int) -> Content)?
    internal val indexRange: IntRange?
    internal val objectContent: ((Any) -> Content)?
    internal val objects: RandomAccessCollection<Any>?
    internal val identifier: ((Any) -> AnyHashable)?

    internal constructor(indexRange: IntRange? = null, indexedContent: ((Int) -> Content)? = null, objects: RandomAccessCollection<Any>? = null, identifier: ((Any) -> AnyHashable)? = null, objectContent: ((Any) -> Content)? = null) {
        this.indexRange = indexRange
        this.indexedContent = indexedContent
        this.objects = objects.sref()
        this.identifier = identifier
        this.objectContent = objectContent
    }

    constructor(data: IntRange, content: (Int) -> Content): this(indexRange = data, indexedContent = content) {
    }

    @Composable
    override fun Compose(context: ComposeContext): ComposeResult {
        ComposeContent(context = context)
        return ComposeResult.ok
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        if (indexRange != null) {
            for (index in indexRange) {
                indexedContent!!(index).Compose(context = context)
            }
        } else if (objects != null) {
            for (object_ in objects.sref()) {
                objectContent!!(object_).Compose(context = context)
            }
        }
    }

    @Composable
    override fun appendListItemViews(to: MutableList<View>, appendingContext: ComposeContext) {
        val views = to
        // ForEach views might or might not contain nested list item factories such as Sections or other ForEach instances.
        // We execute our content closure for the first item in the ForEach and examine its content to see if it contains
        // list item factories. If it does, we perform the full ForEach to append all items so that they can be expanded.
        // If not, we append ourselves instead so that we can take advantage of Compose's ability to specify ranges of items
        var isFirstView = true
        if (indexRange != null) {
            for (index in indexRange) {
                val contentView = indexedContent!!(index)
                if (!appendContentAsListItemViewFactories(contentView = contentView, isFirstView = isFirstView, context = appendingContext)) {
                    views.add(this)
                    return
                } else {
                    isFirstView = false
                }
                contentView.Compose(appendingContext)
            }
        } else if (objects != null) {
            for (object_ in objects.sref()) {
                val contentView = objectContent!!(object_)
                if (!appendContentAsListItemViewFactories(contentView = contentView, isFirstView = isFirstView, context = appendingContext)) {
                    views.add(this)
                    return
                } else {
                    isFirstView = false
                }
                contentView.Compose(appendingContext)
            }
        }
    }

    @Composable
    private fun appendContentAsListItemViewFactories(contentView: View, isFirstView: Boolean, context: ComposeContext): Boolean {
        if (!isFirstView) {
            return true
        }
        var hasViewFactory = false
        val factoryChecker = context.content(composer = ClosureComposer { view, _ -> hasViewFactory = hasViewFactory || view is ListItemFactory })
        contentView.Compose(factoryChecker)
        return hasViewFactory
    }

    override fun ComposeListItems(context: ListItemFactoryContext) {
        if (indexRange != null) {
            context.indexedItems(indexRange, indexedContent!!)
        } else if (objects != null) {
            context.objectItems(objects, identifier!!, objectContent!!)
        }
    }

    companion object {
    }
}


// Kotlin does not support generic constructor parameters, so we have to model many ForEach constructors as functions

//extension ForEach where ID == Data.Element.ID, Content : AccessibilityRotorContent, Data.Element : Identifiable {
//    public init(_ data: Data, @AccessibilityRotorContentBuilder content: @escaping (Data.Element) -> Content) { fatalError() }
//}
fun <D, Content> ForEach(data: RandomAccessCollection<D>, content: (D) -> Content): ForEach<Content> where D: Identifiable<Hashable>, Content: View {
    return ForEach(objects = data as RandomAccessCollection<Any>, identifier = { it -> (it as D).id }, objectContent = { it -> content(it as D) })
}

//extension ForEach where Content : AccessibilityRotorContent {
//    public init(_ data: Data, id: KeyPath<Data.Element, ID>, @AccessibilityRotorContentBuilder content: @escaping (Data.Element) -> Content) { fatalError() }
//}
fun <D, Content> ForEach(data: RandomAccessCollection<D>, id: (D) -> AnyHashable, content: (D) -> Content): ForEach<Content> where Content: View {
    return ForEach(objects = data as RandomAccessCollection<Any>, identifier = { it -> id(it as D) }, objectContent = { it -> content(it as D) })
}


