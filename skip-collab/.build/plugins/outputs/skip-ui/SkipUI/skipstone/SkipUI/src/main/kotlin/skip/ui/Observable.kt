package skip.ui

import skip.lib.*


class SubscriptionView<PublisherType, Content>: View where Content: View {
    val content: Content
    val publisher: PublisherType
    val action: (Any) -> Unit

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(content: Content, publisher: PublisherType, action: (Any) -> Unit) {
        this.content = content.sref()
        this.publisher = publisher.sref()
        this.action = action
    }


    companion object {
    }
}
