// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import skip.lib.*

interface ToolbarContent {
}

interface CustomizableToolbarContent: ToolbarContent {

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun defaultCustomization(defaultVisibility: Visibility = Visibility.automatic, options: ToolbarCustomizationOptions = ToolbarCustomizationOptions.of()): CustomizableToolbarContent = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun customizationBehavior(behavior: ToolbarCustomizationBehavior): CustomizableToolbarContent = this.sref()
}

// We base our toolbar content on `View` rather than a custom protocol so that we can reuse the
// `@ViewBuilder` logic built into the transpiler. The Swift compiler will guarantee that the
// only allowed toolbar content are types that conform to `ToolbarContent`

// Erase the generic ID to facilitate specialized constructor support.
class ToolbarItem<Content>: CustomizableToolbarContent, View where Content: View {
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(placement: ToolbarItemPlacement = ToolbarItemPlacement.automatic, content: () -> Content) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(id: String, placement: ToolbarItemPlacement = ToolbarItemPlacement.automatic, content: () -> Content) {
    }


    companion object {
    }
}

class ToolbarItemGroup<Content>: CustomizableToolbarContent, View where Content: View {
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(placement: ToolbarItemPlacement = ToolbarItemPlacement.automatic, content: () -> Content) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(placement: ToolbarItemPlacement = ToolbarItemPlacement.automatic, content: () -> Content, label: () -> View) {
    }


    companion object {
    }
}

class ToolbarTitleMenu: CustomizableToolbarContent, View {
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor() {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(content: () -> View) {
    }


    companion object {
    }
}

enum class ToolbarCustomizationBehavior: Sendable {
    default,
    reorderable,
    disabled;

    companion object {
    }
}

enum class ToolbarItemPlacement {
    automatic,
    principal,
    navigation,
    primaryAction,
    secondaryAction,
    status,
    confirmationAction,
    cancellationAction,
    destructiveAction,
    keyboard,
    topBarLeading,
    topBarTrailing,
    bottomBar;

    companion object {
    }
}

enum class ToolbarPlacement {
    automatic,
    bottomBar,
    navigationBar,
    tabBar;

    companion object {
    }
}

enum class ToolbarRole: Sendable {
    automatic,
    navigationStack,
    browser,
    editor;

    companion object {
    }
}

enum class ToolbarTitleDisplayMode {
    automatic,
    large,
    inlineLarge,
    inline;

    companion object {
    }
}

class ToolbarCustomizationOptions: OptionSet<ToolbarCustomizationOptions, Int>, Sendable {
    override var rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override val rawvaluelong: ULong
        get() = ULong(rawValue)
    override fun makeoptionset(rawvaluelong: ULong): ToolbarCustomizationOptions = ToolbarCustomizationOptions(rawValue = Int(rawvaluelong))
    override fun assignoptionset(target: ToolbarCustomizationOptions): Unit = assignfrom(target)

    private fun assignfrom(target: ToolbarCustomizationOptions) {
        this.rawValue = target.rawValue
    }

    companion object {

        var alwaysAvailable = ToolbarCustomizationOptions(rawValue = 1 shl 0)
            get() = field.sref({ this.alwaysAvailable = it })
            set(newValue) {
                field = newValue.sref()
            }

        fun of(vararg options: ToolbarCustomizationOptions): ToolbarCustomizationOptions {
            val value = options.fold(Int(0)) { result, option -> result or option.rawValue }
            return ToolbarCustomizationOptions(rawValue = value)
        }
    }
}

