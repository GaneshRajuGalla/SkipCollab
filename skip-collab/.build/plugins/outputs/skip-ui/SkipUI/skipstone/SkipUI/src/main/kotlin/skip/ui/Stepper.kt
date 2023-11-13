// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

// Erase the generic Label to facilitate specialized constructor support.
class Stepper: View {
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(label: () -> View, onIncrement: (() -> Unit)?, onDecrement: (() -> Unit)?, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(value: Binding<Int>, step: Int = 1, label: () -> View, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(value: Binding<Double>, step: Double = 1.0, label: () -> View, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(value: Binding<Int>, in_: IntRange, step: Int = 1, label: () -> View, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(value: Binding<Double>, in_: IntRange, step: Double = 1.0, label: () -> View, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, onIncrement: (() -> Unit)?, onDecrement: (() -> Unit)?, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, onIncrement: (() -> Unit)?, onDecrement: (() -> Unit)?, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, value: Binding<Int>, step: Int = 1, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, value: Binding<Double>, step: Double = 1.0, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, value: Binding<Int>, step: Int = 1, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, value: Binding<Double>, step: Double = 1.0, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, value: Binding<Int>, in_: IntRange, step: Int = 1, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(titleKey: LocalizedStringKey, value: Binding<Double>, in_: IntRange, step: Double = 1.0, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, value: Binding<Int>, in_: IntRange, step: Int = 1, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, value: Binding<Double>, in_: IntRange, step: Double = 1.0, onEditingChanged: (Boolean) -> Unit = { _ ->  }) {
    }


    companion object {
    }
}

