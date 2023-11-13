// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*
import skip.lib.Set

class ViewThatFits<Content>: View where Content: View {
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(in_: Axis.Set = Axis.Set.of(Axis.Set.horizontal, Axis.Set.vertical), content: () -> Content) {
    }


    companion object {
    }
}
