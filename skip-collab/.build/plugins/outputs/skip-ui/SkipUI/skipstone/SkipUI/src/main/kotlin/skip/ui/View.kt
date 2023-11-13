// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import kotlin.reflect.KClass
import skip.lib.*
import skip.lib.Array
import skip.lib.Collection
import skip.lib.Set

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import skip.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.ui.zIndex
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import androidx.compose.foundation.horizontalScroll
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import kotlin.reflect.full.companionObjectInstance
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material3.LocalTextStyle
import skip.foundation.LocalizedStringResource
import skip.foundation.Bundle

interface View {
    fun body(): View = EmptyView()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scenePadding(edges: Edge.Set = Edge.Set.all): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scenePadding(padding: ScenePadding, edges: Edge.Set = Edge.Set.all): View = this.sref()
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun alert(titleKey: LocalizedStringKey, isPresented: Binding<Boolean>, actions: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun alert(title: String, isPresented: Binding<Boolean>, actions: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun alert(title: Text, isPresented: Binding<Boolean>, actions: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun alert(titleKey: LocalizedStringKey, isPresented: Binding<Boolean>, actions: () -> View, message: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun alert(title: String, isPresented: Binding<Boolean>, actions: () -> View, message: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun alert(title: Text, isPresented: Binding<Boolean>, actions: () -> View, message: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> alert(titleKey: LocalizedStringKey, isPresented: Binding<Boolean>, presenting: T?, actions: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> alert(title: String, isPresented: Binding<Boolean>, presenting: T?, actions: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> alert(title: Text, isPresented: Binding<Boolean>, presenting: T?, actions: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> alert(titleKey: LocalizedStringKey, isPresented: Binding<Boolean>, presenting: T?, actions: (T) -> View, message: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> alert(title: String, isPresented: Binding<Boolean>, presenting: T?, actions: (T) -> View, message: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> alert(title: Text, isPresented: Binding<Boolean>, presenting: T?, actions: (T) -> View, message: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <E> alert(isPresented: Binding<Boolean>, error: E?, actions: () -> View): View where E: LocalizedError = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <E> alert(isPresented: Binding<Boolean>, error: E?, actions: (E) -> View, message: (E) -> View): View where E: LocalizedError = this.sref()
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun toolbar(content: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun toolbar(id: String, content: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun toolbar(visibility: Visibility, vararg for_: ToolbarPlacement): View {
        val bars = Array(for_.asIterable())
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun toolbarBackground(style: ShapeStyle, vararg for_: ToolbarPlacement): View {
        val bars = Array(for_.asIterable())
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun toolbarBackground(visibility: Visibility, vararg for_: ToolbarPlacement): View {
        val bars = Array(for_.asIterable())
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun toolbarColorScheme(colorScheme: ColorScheme?, vararg for_: ToolbarPlacement): View {
        val bars = Array(for_.asIterable())
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun toolbarTitleDisplayMode(mode: ToolbarTitleDisplayMode): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun toolbarTitleMenu(content: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun toolbarRole(role: ToolbarRole): View = this.sref()
    fun progressViewStyle(style: ProgressViewStyle): View {
        return environment({ EnvironmentValues.shared.set_progressViewStyle(it) }, style)
    }
    fun disclosureGroupStyle(style: DisclosureGroupStyle): View {
        // We only support the single .automatic style
        return this.sref()
    }
    fun formStyle(style: FormStyle): View = this.sref()
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun deleteDisabled(isDisabled: Boolean): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun listRowSeparator(visibility: Visibility, edges: VerticalEdge.Set = VerticalEdge.Set.all): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun listRowSeparatorTint(color: Color?, edges: VerticalEdge.Set = VerticalEdge.Set.all): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun listSectionSeparator(visibility: Visibility, edges: VerticalEdge.Set = VerticalEdge.Set.all): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun listSectionSeparatorTint(color: Color?, edges: VerticalEdge.Set = VerticalEdge.Set.all): View = this.sref()

    fun listStyle(style: ListStyle): View {
        return environment({ EnvironmentValues.shared.set_listStyle(it) }, style)
    }

    fun listItemTint(tint: Color?): View {
        return environment({ EnvironmentValues.shared.set_listItemTint(it) }, tint)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun listItemTint(tint: ListItemTint?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun listRowInsets(insets: EdgeInsets?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun listRowSpacing(spacing: Double?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun listSectionSpacing(spacing: ListSectionSpacing): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun listSectionSpacing(spacing: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun moveDisabled(isDisabled: Boolean): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun swipeActions(edge: HorizontalEdge = HorizontalEdge.trailing, allowsFullSwipe: Boolean = true, content: () -> View): View = this.sref()
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun navigationBarBackButtonHidden(hidesBackButton: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun navigationBarTitleDisplayMode(displayMode: NavigationBarItem.TitleDisplayMode): View = this.sref()

    fun <D, V> navigationDestination(for_: KClass<D>, destination: (D) -> V): View where D: Any, V: View {
        val data = for_
        val destinations: Dictionary<KClass<*>, NavigationDestination> = dictionaryOf(Tuple2(data, NavigationDestination(destination = { it -> destination(it as D) })))
        return preference(key = NavigationDestinationsPreferenceKey::class, value = destinations)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <V> navigationDestination(isPresented: Binding<Boolean>, destination: () -> V): View where V: View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <D, C> navigationDestination(item: Binding<D?>, destination: (D) -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun navigationDocument(url: URL): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun navigationSplitViewColumnWidth(width: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun navigationSplitViewColumnWidth(min: Double? = null, ideal: Double, max: Double? = null): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun navigationSplitViewStyle(style: NavigationSplitViewStyle): View = this.sref()

    fun navigationTitle(title: Text): View = navigationTitle(title.text)

    fun navigationTitle(title: String): View = preference(key = NavigationTitlePreferenceKey::class, value = title)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun navigationTitle(title: Binding<String>): View = this.sref()
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun contentMargins(edges: Edge.Set = Edge.Set.all, insets: EdgeInsets, for_: ContentMarginPlacement = ContentMarginPlacement.automatic): View {
        val placement = for_
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun contentMargins(edges: Edge.Set = Edge.Set.all, length: Double?, for_: ContentMarginPlacement = ContentMarginPlacement.automatic): View {
        val placement = for_
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun contentMargins(length: Double, for_: ContentMarginPlacement = ContentMarginPlacement.automatic): View {
        val placement = for_
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollBounceBehavior(behavior: ScrollBounceBehavior, axes: Axis.Set = Axis.Set.of(Axis.Set.vertical)): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollClipDisabled(disabled: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollContentBackground(visibility: Visibility): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollDismissesKeyboard(mode: ScrollDismissesKeyboardMode): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollDisabled(disabled: Boolean): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollIndicators(visibility: ScrollIndicatorVisibility, axes: Axis.Set = Axis.Set.of(Axis.Set.vertical, Axis.Set.horizontal)): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollIndicatorsFlash(onAppear: Boolean): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollIndicatorsFlash(trigger: Equatable): View {
        val value = trigger
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollPosition(id: Binding<Hashable?>): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollPosition(initialAnchor: UnitPoint?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollTarget(isEnabled: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollTargetBehavior(behavior: Any): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scrollTargetLayout(isEnabled: Boolean = true): View = this.sref()
    fun tabItem(label: () -> View): View = TabItem(view = this, label = label)

    fun tabViewStyle(style: TabViewStyle): View {
        // We only support .automatic
        return this.sref()
    }
    fun buttonStyle(style: ButtonStyle): View {
        return environment({ EnvironmentValues.shared.set_buttonStyle(it) }, style)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun buttonRepeatBehavior(behavior: ButtonRepeatBehavior): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun buttonBorderShape(shape: Any): View = this.sref()
    fun controlGroupStyle(style: ControlGroupStyle): View = this.sref()
    fun datePickerStyle(style: DatePickerStyle): View {
        // We only support .automatic
        return this.sref()
    }
    fun pickerStyle(style: PickerStyle): View = this.sref()
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onSubmit(of: SubmitTriggers = SubmitTriggers.text, action: () -> Unit): View {
        val triggers = of
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun submitScope(isBlocking: Boolean = true): View = this.sref()

    fun textFieldStyle(style: TextFieldStyle): View {
        // We only support Android's outline style
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun textInputAutocapitalization(autocapitalization: TextInputAutocapitalization?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun keyboardType(type: UIKeyboardType): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun textContentType(textContentType: UITextContentType?): View = this.sref()
    fun toggleStyle(style: ToggleStyle): View {
        // We only support Android's Switch control
        return this.sref()
    }

    fun environmentObject(object_: Any): View = environmentObject(type = type(of = object_), object_ = object_)

    // Must be public to allow access from our inline `environment` function.
    fun environmentObject(type: KClass<*>, object_: Any?): View {
        return ComposeModifierView(contentView = this) { view, context ->
            val compositionLocal = EnvironmentValues.shared.objectCompositionLocal(type = type)
            val value = (object_ ?: Unit).sref()
            val provided = compositionLocal provides value
            CompositionLocalProvider(provided) { view.Compose(context = context) }
        }
    }

    // We rely on the transpiler to turn the `WriteableKeyPath` provided in code into a `setValue` closure
    fun <V> environment(setValue: (V) -> Unit, value: V): View {
        return ComposeModifierView(contentView = this) { view, context ->
            EnvironmentValues.shared.setValues({ _ -> setValue(value) }, in_ = { view.Compose(context = context) })
        }
    }
    fun preference(key: KClass<*>, value: Any): View {
        return ComposeModifierView(contentView = this) { view, context ->
            val pvalue = remember { mutableStateOf<Any?>(null) }
            PreferenceValues.shared.preference(key = key)?.reduce(savedValue = pvalue.value, newValue = value)
            pvalue.value = value
            view.Compose(context = context)
        }
    }
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <Item, Content> fullScreenCover(item: Binding<Item?>, onDismiss: (() -> Unit)? = null, content: (Item) -> Content): View where Content: View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <Content> fullScreenCover(isPresented: Binding<Boolean>, onDismiss: (() -> Unit)? = null, content: () -> Content): View where Content: View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun presentationDetents(detents: Set<PresentationDetent>): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun presentationDetents(detents: Set<PresentationDetent>, selection: Binding<PresentationDetent>): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun presentationDragIndicator(visibility: Visibility): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun presentationBackgroundInteraction(interaction: PresentationBackgroundInteraction): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun presentationCompactAdaptation(adaptation: PresentationAdaptation): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun presentationCompactAdaptation(horizontal: PresentationAdaptation, vertical: PresentationAdaptation): View {
        val horizontalAdaptation = horizontal
        val verticalAdaptation = vertical
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun presentationCornerRadius(cornerRadius: Double?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun presentationContentInteraction(behavior: PresentationContentInteraction): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun presentationBackground(style: ShapeStyle): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun presentationBackground(alignment: Alignment = Alignment.center, content: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <Item, Content> sheet(item: Binding<Item?>, onDismiss: (() -> Unit)? = null, content: (Item) -> Content): View where Content: View = this.sref()

    fun <Content> sheet(isPresented: Binding<Boolean>, onDismiss: (() -> Unit)? = null, content: () -> Content): View where Content: View {
        return ComposeModifierView(contentView = this) { view, context ->
            SheetPresentation(isPresented = isPresented, content = content, context = context, onDismiss = onDismiss)
            view.ComposeContent(context = context)
        }
    }
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun defaultAppStorage(store: UserDefaults): View = this.sref()
    fun accessibilityIdentifier(identifier: String): View {
        return ComposeModifierView(contextView = this, role = ComposeModifierRole.accessibility) { it -> it.value.modifier = it.value.modifier.testTag(identifier) }
    }
    fun accessibilityLabel(label: Text): View {
        return ComposeModifierView(contextView = this, role = ComposeModifierRole.accessibility) { it ->
            it.value.modifier = it.value.modifier.semantics { contentDescription = label.text }
        }
    }

    fun accessibilityLabel(label: String): View {
        return ComposeModifierView(contextView = this, role = ComposeModifierRole.accessibility) { it ->
            it.value.modifier = it.value.modifier.semantics { contentDescription = label }
        }
    }
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun gesture(gesture: Gesture): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun gesture(gesture: Gesture, including: GestureMask): View {
        val mask = including
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun highPriorityGesture(gesture: Gesture, including: GestureMask = GestureMask.all): View {
        val mask = including
        return this.sref()
    }

    fun onLongPressGesture(minimumDuration: Double = 0.5, maximumDistance: Double = Double(10.0), perform: () -> Unit): View {
        val action = perform
        return GestureModifierView(contextView = this, longPressAction = action)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onLongPressGesture(minimumDuration: Double = 0.5, maximumDistance: Double = Double(10.0), perform: () -> Unit, onPressingChanged: (Boolean) -> Unit): View {
        val action = perform
        return this.sref()
    }

    fun onTapGesture(count: Int = 1, perform: (CGPoint) -> Unit): View {
        val action = perform
        if (count == 1) {
            return GestureModifierView(contextView = this, tapAction = action)
        } else if (count == 2) {
            return GestureModifierView(contextView = this, doubleTapAction = action)
        } else {
            return this.sref()
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onTapGesture(count: Int = 1, coordinateSpace: CoordinateSpaceProtocol, perform: (CGPoint) -> Unit): View {
        val action = perform
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> simultaneousGesture(gesture: Gesture, including: GestureMask = GestureMask.all): View {
        val mask = including
        return this.sref()
    }
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun userActivity(activityType: String, isActive: Boolean = true, update: (Any) -> Unit): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <P> userActivity(activityType: String, element: P?, update: (P, Any) -> Unit): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onContinueUserActivity(activityType: String, perform: (Any) -> Unit): View {
        val action = perform
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onOpenURL(perform: (URL) -> Unit): View {
        val action = perform
        return this.sref()
    }
    fun labelStyle(style: LabelStyle): View {
        // We only support .automatic
        return this.sref()
    }

    fun labeledContentStyle(style: LabeledContentStyle): View = this.sref()
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun allowsTightening(flag: Boolean): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun autocorrectionDisabled(disable: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun baselineOffset(baselineOffset: Double): View = this.sref()

    fun bold(isActive: Boolean = true): View = fontWeight(if (isActive) Font.Weight.bold else null)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun dynamicTypeSize(size: DynamicTypeSize): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun dynamicTypeSize(range: IntRange): View = this.sref()

    fun font(font: Font): View {
        return environment({ EnvironmentValues.shared.setfont(it) }, font)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun fontDesign(design: Font.Design?): View = this.sref()

    fun fontWeight(weight: Font.Weight?): View {
        return environment({ EnvironmentValues.shared.set_fontWeight(it) }, weight)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun fontWidth(width: Font.Width?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun invalidatableContent(invalidatable: Boolean = true): View = this.sref()

    fun italic(isActive: Boolean = true): View {
        return environment({ EnvironmentValues.shared.set_isItalic(it) }, isActive)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun kerning(kerning: Double): View = this.sref()

    fun lineLimit(number: Int?): View {
        return environment({ EnvironmentValues.shared.setlineLimit(it) }, number)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun lineLimit(limit: IntRange): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun lineLimit(limit: Int, reservesSpace: Boolean): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun lineSpacing(lineSpacing: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun monospacedDigit(): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun monospaced(isActive: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun minimumScaleFactor(factor: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun multilineTextAlignment(alignment: TextAlignment): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun privacySensitive(sensitive: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun redacted(reason: RedactionReasons): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun speechAlwaysIncludesPunctuation(value: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun speechSpellsOutCharacters(value: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun speechAdjustedPitch(value: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun speechAnnouncementsQueued(value: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun strikethrough(isActive: Boolean = true, pattern: Text.LineStyle.Pattern = Text.LineStyle.Pattern.solid, color: Color? = null): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun textCase(textCase: Text.Case?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun textScale(scale: Text.Scale, isEnabled: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun textSelection(selectability: TextSelectability): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun tracking(tracking: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun truncationMode(mode: Text.TruncationMode): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun underline(isActive: Boolean = true, pattern: Text.LineStyle.Pattern = Text.LineStyle.Pattern.solid, color: Color? = null): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun unredacted(): View = this.sref()
    fun textEditorStyle(style: TextEditorStyle): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun findNavigator(isPresented: Binding<Boolean>): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun findDisabled(isDisabled: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun replaceDisabled(isDisabled: Boolean = true): View = this.sref()
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <P> onReceive(publisher: P, perform: (Any) -> Unit): View {
        val action = perform
        return this.sref()
    }
    /// Compose this view without an existing context - typically called when integrating a SwiftUI view tree into pure Compose.
    @Composable
    fun Compose(): ComposeResult = Compose(context = ComposeContext())

    /// Calls to `Compose` are added by the transpiler.
    @Composable
    fun Compose(context: ComposeContext): ComposeResult {
        val matchtarget_0 = context.composer
        if (matchtarget_0 != null) {
            val composer = matchtarget_0
            composer.Compose(view = this, context = l@{ retain ->
                if (retain) {
                    return@l context
                }
                var context = context.sref()
                context.composer = null
                return@l context
            })
        } else {
            ComposeContent(context = context)
        }
        return ComposeResult.ok
    }

    /// Compose this view's content.
    @Composable
    fun ComposeContent(context: ComposeContext): Unit = body().ComposeContent(context)

    /// Strip modifier views.
    ///
    /// - Parameter until: Return `true` to stop stripping at a modifier with a given role.
    fun <R> strippingModifiers(until: (ComposeModifierRole) -> Boolean = { _ -> false }, perform: (View?) -> R): R = perform(this)
    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun allowsHitTesting(enabled: Boolean): View = this.sref()

    fun aspectRatio(ratio: Double? = null, contentMode: ContentMode): View {
        return environment({ EnvironmentValues.shared.set_aspectRatio(it) }, Tuple2(ratio, contentMode))
    }

    fun aspectRatio(size: CGSize, contentMode: ContentMode): View = aspectRatio(size.width / size.height, contentMode = contentMode)

    fun background(color: Color): View {
        return ComposeModifierView(contextView = this) { it -> it.value.modifier = it.value.modifier.background(color.colorImpl()) }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <V> background(alignment: Alignment = Alignment.center, content: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun background(ignoresSafeAreaEdges: Edge.Set = Edge.Set.all): View {
        val edges = ignoresSafeAreaEdges
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun background(style: ShapeStyle, ignoresSafeAreaEdges: Edge.Set = Edge.Set.all): View {
        val edges = ignoresSafeAreaEdges
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <S> background(in_: Shape, fillStyle: FillStyle = FillStyle()): View {
        val shape = in_
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun background(style: ShapeStyle, in_: Shape, fillStyle: FillStyle = FillStyle()): View {
        val shape = in_
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun backgroundStyle(style: ShapeStyle): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun badge(count: Int): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun badge(label: Text?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun badge(key: LocalizedStringKey): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun badge(label: String): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun badgeProminence(prominence: BadgeProminence): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun blendMode(blendMode: BlendMode): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun blur(radius: Double, opaque: Boolean = false): View = this.sref()

    fun border(color: Color, width: Double = 1.0): View {
        return ComposeModifierView(contextView = this) { it -> it.value.modifier = it.value.modifier.border(width = width.dp, color = color.colorImpl()) }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun border(style: ShapeStyle, width: Double = 1.0): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun brightness(amount: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun clipShape(shape: Shape, style: FillStyle = FillStyle()): View = this.sref()

    fun clipped(antialiased: Boolean = false): View {
        return ComposeModifierView(contextView = this) { it -> it.value.modifier = it.value.modifier.clipToBounds() }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun colorInvert(): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun colorMultiply(color: Color): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun compositingGroup(): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun confirmationDialog(titleKey: LocalizedStringKey, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, actions: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun confirmationDialog(title: String, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, actions: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun confirmationDialog(title: Text, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, actions: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun confirmationDialog(titleKey: LocalizedStringKey, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, actions: () -> View, message: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun confirmationDialog(title: String, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, actions: () -> View, message: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun confirmationDialog(title: Text, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, actions: () -> View, message: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> confirmationDialog(titleKey: LocalizedStringKey, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, presenting: T?, actions: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> confirmationDialog(title: String, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, presenting: T?, actions: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> confirmationDialog(title: Text, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, presenting: T?, actions: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> confirmationDialog(titleKey: LocalizedStringKey, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, presenting: T?, actions: (T) -> View, message: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> confirmationDialog(title: String, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, presenting: T?, actions: (T) -> View, message: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> confirmationDialog(title: Text, isPresented: Binding<Boolean>, titleVisibility: Visibility = Visibility.automatic, presenting: T?, actions: (T) -> View, message: (T) -> View): View {
        val data = presenting
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun containerBackground(style: ShapeStyle, for_: ContainerBackgroundPlacement): View {
        val container = for_
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun containerBackground(for_: ContainerBackgroundPlacement, alignment: Alignment = Alignment.center, content: () -> View): View {
        val container = for_
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun containerRelativeFrame(axes: Axis.Set, alignment: Alignment = Alignment.center): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun containerRelativeFrame(axes: Axis.Set, count: Int, span: Int = 1, spacing: Double, alignment: Alignment = Alignment.center): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun containerRelativeFrame(axes: Axis.Set, alignment: Alignment = Alignment.center, length: (Double, Axis) -> Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> containerShape(shape: Shape): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun contentShape(shape: Shape, eoFill: Boolean = false): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun contentShape(kind: ContentShapeKinds, shape: Shape, eoFill: Boolean = false): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun contextMenu(menuItems: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun contextMenu(menuItems: () -> View, preview: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <I> contextMenu(forSelectionType: KClass<*>? = null, menu: (Set<I>) -> View, primaryAction: ((Set<I>) -> Unit)? = null): View {
        val itemType = forSelectionType
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun contrast(amount: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun controlSize(controlSize: ControlSize): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun coordinateSpace(name: NamedCoordinateSpace): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun defaultHoverEffect(effect: HoverEffect?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun defersSystemGestures(on: Edge.Set): View {
        val edges = on
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun dialogSuppressionToggle(titleKey: LocalizedStringKey, isSuppressed: Binding<Boolean>): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun dialogSuppression(title: String, isSuppressed: Binding<Boolean>): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun dialogSuppressionToggle(label: Text, isSuppressed: Binding<Boolean>): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun dialogSuppressionToggle(isSuppressed: Binding<Boolean>): View = this.sref()


    fun disabled(disabled: Boolean): View {
        return environment({ EnvironmentValues.shared.setisEnabled(it) }, !disabled)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun drawingGroup(opaque: Boolean = false, colorMode: ColorRenderingMode = ColorRenderingMode.nonLinear): View = this.sref()

    fun equatable(): View = EquatableView(content = this)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun fileMover(isPresented: Binding<Boolean>, file: URL?, onCompletion: (Result<URL, Error>) -> Unit): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun fileMover(isPresented: Binding<Boolean>, files: Collection<URL>, onCompletion: (Result<Array<URL>, Error>) -> Unit): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun fileMover(isPresented: Binding<Boolean>, file: URL?, onCompletion: (Result<URL, Error>) -> Unit, onCancellation: () -> Unit): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun fileMover(isPresented: Binding<Boolean>, files: Collection<URL>, onCompletion: (Result<Array<URL>, Error>) -> Unit, onCancellation: () -> Unit): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun fixedSize(horizontal: Boolean, vertical: Boolean): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun fixedSize(): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun flipsForRightToLeftLayoutDirection(enabled: Boolean): View = this.sref()

    fun foregroundColor(color: Color?): View {
        return environment({ EnvironmentValues.shared.set_color(it) }, color)
    }

    fun foregroundStyle(color: Color): View = foregroundColor(color)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun foregroundStyle(style: ShapeStyle): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun foregroundStyle(primary: ShapeStyle, secondary: ShapeStyle): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun foregroundStyle(primary: ShapeStyle, secondary: ShapeStyle, tertiary: ShapeStyle): View = this.sref()

    fun frame(width: Double? = null, height: Double? = null): View {
        return ComposeModifierView(contentView = this) { view, context ->
            var context = context.sref()
            if (width != null) {
                context.modifier = context.modifier.width(width.dp)
            }
            if (height != null) {
                context.modifier = context.modifier.height(height.dp)
            }
            EnvironmentValues.shared.setValues({ it ->
                if (width != null) {
                    it.set_fillWidth(null)
                    it.set_fillWidthModifier(null)
                }
                if (height != null) {
                    it.set_fillHeight(null)
                    it.set_fillHeightModifier(null)
                }
            }, in_ = { view.ComposeContent(context = context) })
        }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun frame(width: Double? = null, height: Double? = null, alignment: Alignment): View = frame(width = width, height = height)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun frame(minWidth: Double? = null, idealWidth: Double? = null, maxWidth: Double? = null, minHeight: Double? = null, idealHeight: Double? = null, maxHeight: Double? = null, alignment: Alignment = Alignment.center): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun grayscale(amount: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun handlesExternalEvents(preferring: Set<String>, allowing: Set<String>): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun headerProminence(prominence: Prominence): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun help(textKey: LocalizedStringKey): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun help(text: Text): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun help(text: String): View = this.sref()

    fun hidden(): View = opacity(0.0)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun hoverEffect(effect: HoverEffect = HoverEffect.automatic, isEnabled: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun hoverEffectDisabled(disabled: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun hueRotation(angle: Angle): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun id(id: Hashable): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun ignoresSafeArea(regions: SafeAreaRegions = SafeAreaRegions.all, edges: Edge.Set = Edge.Set.all): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun inspector(isPresented: Binding<Boolean>, content: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun inspectorColumnWidth(min: Double? = null, ideal: Double, max: Double? = null): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun inspectorColumnWidth(width: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun interactionActivityTrackingTag(tag: String): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun interactiveDismissDisabled(isDisabled: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun keyboardShortcut(key: KeyEquivalent, modifiers: EventModifiers = EventModifiers.command): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun keyboardShortcut(shortcut: KeyboardShortcut?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun keyboardShortcut(key: KeyEquivalent, modifiers: EventModifiers = EventModifiers.command, localization: KeyboardShortcut.Localization): View = this.sref()

    fun labelsHidden(): View {
        return environment({ EnvironmentValues.shared.set_labelsHidden(it) }, true)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun layoutDirectionBehavior(behavior: LayoutDirectionBehavior): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun layoutPriority(value: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun listRowBackground(view: View?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun luminanceToAlpha(): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun mask(alignment: Alignment = Alignment.center, mask: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <ID> matchedGeometryEffect(id: Hashable, in_: Any, properties: MatchedGeometryProperties = MatchedGeometryProperties.frame, anchor: UnitPoint = UnitPoint.center, isSource: Boolean = true): View {
        val namespace = in_
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun modifier(modifier: Any): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun offset(offset: CGSize): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun offset(x: Double = 0.0, y: Double = 0.0): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onAppear(perform: (() -> Unit)? = null): View {
        val action = perform
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <V> onChange(of: V, perform: (V) -> Unit): View {
        val value = of
        val action = perform
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <V> onChange(of: V, initial: Boolean = false, action: (V, V) -> Unit): View {
        val value = of
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <V> onChange(of: V, initial: Boolean = false, action: () -> Unit): View {
        val value = of
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onContinuousHover(coordinateSpace: CoordinateSpaceProtocol = CoordinateSpaceProtocol.local, perform: (HoverPhase) -> Unit): View {
        val action = perform
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onDisappear(perform: (() -> Unit)? = null): View {
        val action = perform
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun onHover(perform: (Boolean) -> Unit): View {
        val action = perform
        return this.sref()
    }

    fun opacity(opacity: Double): View {
        return ComposeModifierView(contextView = this) { it -> it.value.modifier = it.value.modifier.alpha(Float(opacity)) }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun overlay(alignment: Alignment = Alignment.center, content: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun overlay(style: ShapeStyle, ignoresSafeAreaEdges: Edge.Set = Edge.Set.all): View {
        val edges = ignoresSafeAreaEdges
        return this.sref()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun overlay(style: ShapeStyle, in_: Shape, fillStyle: FillStyle = FillStyle()): View {
        val shape = in_
        return this.sref()
    }

    fun padding(insets: EdgeInsets): View {
        return ComposeModifierView(contextView = this, role = ComposeModifierRole.spacing) { it ->
            // Compose throws a runtime error for negative padding
            it.value.modifier = it.value.modifier.padding(start = max(insets.leading, 0.0).dp, top = max(insets.top, 0.0).dp, end = max(insets.trailing, 0.0).dp, bottom = max(insets.bottom, 0.0).dp)
        }
    }

    fun padding(edges: Edge.Set = Edge.Set.all, length: Double? = null): View {
        val amount = max(length ?: Double(16.0), Double(0.0)).dp.sref()
        val start = (if (edges.contains(Edge.Set.leading)) amount else 0.dp).sref()
        val end = (if (edges.contains(Edge.Set.trailing)) amount else 0.dp).sref()
        val top = (if (edges.contains(Edge.Set.top)) amount else 0.dp).sref()
        val bottom = (if (edges.contains(Edge.Set.bottom)) amount else 0.dp).sref()
        return ComposeModifierView(contextView = this, role = ComposeModifierRole.spacing) { it -> it.value.modifier = it.value.modifier.padding(start = start, top = top, end = end, bottom = bottom) }
    }

    fun padding(length: Double): View = padding(Edge.Set.all, length)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun persistentSystemOverlays(visibility: Visibility): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun position(position: CGPoint): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun position(x: Double = 0.0, y: Double = 0.0): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun preferredColorScheme(colorScheme: ColorScheme?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun projectionEffect(transform: Any): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <Item> popover(item: Binding<Item?>, attachmentAnchor: Any? = null, arrowEdge: Edge = Edge.top, content: (Item) -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun popover(isPresented: Binding<Boolean>, attachmentAnchor: Any? = null, arrowEdge: Edge = Edge.top, content: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun refreshable(action: suspend () -> Unit): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun renameAction(isFocused: Any): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun renameAction(action: () -> Unit): View = this.sref()

    fun rotationEffect(angle: Angle): View {
        return ComposeModifierView(contextView = this) { it -> it.value.modifier = it.value.modifier.rotate(Float(angle.degrees)) }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun rotationEffect(angle: Angle, anchor: UnitPoint): View {
        fatalError()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun rotation3DEffect(angle: Angle, axis: Tuple3<Double, Double, Double>, anchor: UnitPoint = UnitPoint.center, anchorZ: Double = 0.0, perspective: Double = 1.0): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun safeAreaInset(edge: VerticalEdge, alignment: HorizontalAlignment = HorizontalAlignment.center, spacing: Double? = null, content: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun safeAreaInset(edge: HorizontalEdge, alignment: VerticalAlignment = VerticalAlignment.center, spacing: Double? = null, content: () -> View): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun safeAreaPadding(insets: EdgeInsets): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun safeAreaPadding(edges: Edge.Set = Edge.Set.all, length: Double? = null): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun safeAreaPadding(length: Double): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun saturation(amount: Double): View = this.sref()

    fun scaledToFit(): View = aspectRatio(null, contentMode = ContentMode.fit)

    fun scaledToFill(): View = aspectRatio(null, contentMode = ContentMode.fill)

    fun scaleEffect(scale: CGSize): View = scaleEffect(x = scale.width, y = scale.height)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scaleEffect(scale: CGSize, anchor: UnitPoint): View = scaleEffect(x = scale.width, y = scale.height)

    fun scaleEffect(s: Double): View = scaleEffect(x = s, y = s)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scaleEffect(s: Double, anchor: UnitPoint): View = scaleEffect(x = s, y = s)

    fun scaleEffect(x: Double = 1.0, y: Double = 1.0): View {
        return ComposeModifierView(contextView = this) { it -> it.value.modifier = it.value.modifier.scale(scaleX = Float(x), scaleY = Float(y)) }
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun scaleEffect(x: Double = 1.0, y: Double = 1.0, anchor: UnitPoint): View = scaleEffect(x = x, y = y)

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun selectionDisabled(isDisabled: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun sensoryFeedback(feedback: SensoryFeedback, trigger: Equatable): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> sensoryFeedback(feedback: SensoryFeedback, trigger: T, condition: (T, T) -> Boolean): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <T> sensoryFeedback(trigger: T, feedback: (T, T) -> SensoryFeedback?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun shadow(color: Color = Color(white = 0.0, opacity = 0.33), radius: Double, x: Double = 0.0, y: Double = 0.0): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun statusBarHidden(hidden: Boolean = true): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun submitLabel(submitLabel: SubmitLabel): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun symbolEffectsRemoved(isEnabled: Boolean = true): View = this.sref()

    fun tag(tag: Hashable): View = this.sref()

    fun task(priority: TaskPriority = TaskPriority.userInitiated, action: suspend () -> Unit): View = task(id = 0, priority = priority, action)

    fun task(id: Any, priority: TaskPriority = TaskPriority.userInitiated, action: suspend () -> Unit): View {
        val value = id
        return ComposeModifierView(contentView = this) { view, context ->
            val handler = rememberUpdatedState(action)
            LaunchedEffect(value) { handler.value() }
            view.Compose(context = context)
        }
    }

    fun tint(color: Color?): View {
        return environment({ EnvironmentValues.shared.set_tint(it) }, color)
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun tint(tint: ShapeStyle?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun transformEffect(transform: Any): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun <V> transformEnvironment(keyPath: Any, transform: (InOut<V>) -> Unit): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun typeSelectEquivalent(text: Text?): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun typeSelectEquivalent(stringKey: LocalizedStringKey): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun typeSelectEquivalent(string: String): View = this.sref()

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    fun zIndex(value: Double): View = this.sref()
}
// Use inline final func to get reified generic type

inline fun <reified T> View.environment(object_: T?): View = environmentObject(type = T::class, object_ = object_)


