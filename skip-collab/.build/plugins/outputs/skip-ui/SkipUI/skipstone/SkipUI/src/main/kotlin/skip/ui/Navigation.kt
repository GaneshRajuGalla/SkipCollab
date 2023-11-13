// This is free software: you can redistribute and/or modify it
// under the terms of the GNU Lesser General Public License 3.0
// as published by the Free Software Foundation https://fsf.org

package skip.ui

import kotlin.reflect.KClass
import skip.lib.*
import skip.lib.Array

import skip.foundation.*
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
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

class NavigationStack<Root>: View where Root: View {
    private val root: Root

    constructor(root: () -> Root) {
        this.root = root()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(path: Any, root: () -> Root) {
        this.root = root()
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        val preferenceUpdates = remember { mutableStateOf(0) }
        preferenceUpdates.value.sref() // Read so that it can trigger recompose on change
        val preferencesDidChange = { preferenceUpdates.value += 1 }

        // Have to use rememberSaveable for e.g. a nav stack in each tab
        val destinations = rememberSaveable(stateSaver = context.stateSaver as Saver<Dictionary<KClass<*>, NavigationDestination>, Any>) { mutableStateOf(NavigationDestinationsPreferenceKey.defaultValue) }
        val navController = rememberNavController()
        val navigator = rememberSaveable(stateSaver = context.stateSaver as Saver<Navigator, Any>) { mutableStateOf(Navigator(navController = navController, destinations = destinations.value)) }
        navigator.value.didCompose(navController = navController, destinations = destinations.value)

        val providedNavigator = LocalNavigator provides navigator.value
        CompositionLocalProvider(providedNavigator) {
            ComposeContainer(modifier = context.modifier, fillWidth = true, fillHeight = true) { modifier ->
                NavHost(navController = navController, startDestination = Navigator.rootRoute, modifier = modifier) {
                    composable(route = Navigator.rootRoute, exitTransition = {
                        slideOutHorizontally(targetOffsetX = { it -> it * -1 / 3 })
                    }, popEnterTransition = {
                        slideInHorizontally(initialOffsetX = { it -> it * -1 / 3 })
                    }) { entry ->
                        navigator.value.state(for_ = entry)?.let { state ->
                            val entryContext = context.content(stateSaver = state.stateSaver)
                            ComposeEntry(navController = navController, destinations = destinations, destinationsDidChange = preferencesDidChange, isRoot = true, context = entryContext) { context -> root.Compose(context = context) }
                        }
                    }
                    for (destinationIndex in 0 until Navigator.destinationCount) {
                        composable(route = Navigator.route(for_ = destinationIndex, valueString = "{identifier}"), arguments = listOf(navArgument("identifier") { type = NavType.StringType }), enterTransition = {
                            slideInHorizontally(initialOffsetX = { it -> it })
                        }, exitTransition = {
                            slideOutHorizontally(targetOffsetX = { it -> it * -1 / 3 })
                        }, popEnterTransition = {
                            slideInHorizontally(initialOffsetX = { it -> it * -1 / 3 })
                        }, popExitTransition = {
                            slideOutHorizontally(targetOffsetX = { it -> it })
                        }) { entry ->
                            navigator.value.state(for_ = entry)?.let { state ->
                                state.targetValue.sref()?.let { targetValue ->
                                    val entryContext = context.content(stateSaver = state.stateSaver)
                                    EnvironmentValues.shared.setValues({ it ->
                                        it.setdismiss({ navController.popBackStack() })
                                    }, in_ = {
                                        ComposeEntry(navController = navController, destinations = destinations, destinationsDidChange = preferencesDidChange, isRoot = false, context = entryContext) { context -> state.destination?.invoke(targetValue)?.Compose(context = context) }
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ComposeEntry(navController: NavHostController, destinations: MutableState<Dictionary<KClass<*>, NavigationDestination>>, destinationsDidChange: () -> Unit, isRoot: Boolean, context: ComposeContext, content: @Composable (ComposeContext) -> Unit) {
        val preferenceUpdates = remember { mutableStateOf(0) }
        preferenceUpdates.value.sref() // Read so that it can trigger recompose on change

        val uncomposedTitle = "__UNCOMPOSED__"
        val title = rememberSaveable(stateSaver = context.stateSaver as Saver<String, Any>) { mutableStateOf(uncomposedTitle) }

        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        var modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).then(context.modifier)
        // Perform an invisible compose pass to gather preference information. Otherwise we may see the content render one way, then
        // immediately re-render with an updated top bar
        if (title.value == uncomposedTitle) {
            modifier = modifier.alpha(0.0f)
        }

        // We place the top bar scaffold within each entry rather than at the navigation controller level. There isn't a fluid animation
        // between navigation bar states on Android, and it is simpler to only hoist navigation bar preferences to this level
        Scaffold(modifier = modifier, topBar = l@{
            if (isRoot && title.value.isEmpty) {
                return@l
            }
            MediumTopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.systemBackground.colorImpl(), titleContentColor = MaterialTheme.colorScheme.onSurface), title = { androidx.compose.material3.Text(title.value, maxLines = 1, overflow = TextOverflow.Ellipsis) }, navigationIcon = {
                if (!isRoot) {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back") }
                }
            }, scrollBehavior = scrollBehavior)
        }) { padding ->
            // Provide our current destinations as the initial value so that we don't forget previous destinations. Only one navigation entry
            // will be composed, and we want to retain destinations from previous entries
            val destinationsPreference = Preference<Dictionary<KClass<*>, NavigationDestination>>(key = NavigationDestinationsPreferenceKey::class, initialValue = destinations.value, update = { it -> destinations.value = it }, didChange = destinationsDidChange)
            val titlePreference = Preference<String>(key = NavigationTitlePreferenceKey::class, update = { it -> title.value = it }, didChange = { preferenceUpdates.value += 1 })
            PreferenceValues.shared.collectPreferences(arrayOf(destinationsPreference, titlePreference)) {
                // Only use the top padding; the Scaffold will also set bottom padding matching the home swipe area
                Box(modifier = Modifier.padding(top = padding.calculateTopPadding()).fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) { content(context.content()) }
            }
            if (title.value == uncomposedTitle) {
                title.value = NavigationTitlePreferenceKey.defaultValue
            }
        }
    }

    companion object {
    }
}

internal typealias NavigationDestinations = Dictionary<KClass<*>, NavigationDestination>
internal class NavigationDestination {
    internal val destination: (Any) -> View
    // No way to compare closures. Assume equal so we don't think our destinations are constantly updating
    override fun equals(other: Any?): Boolean = true

    constructor(destination: (Any) -> View) {
        this.destination = destination
    }
}

@Stable
internal open class Navigator {

    private var navController: NavHostController
        get() = field.sref({ this.navController = it })
        set(newValue) {
            field = newValue.sref()
        }
    private var destinations: Dictionary<KClass<*>, NavigationDestination>
        get() = field.sref({ this.destinations = it })
        set(newValue) {
            field = newValue.sref()
        }

    private var destinationIndexes: Dictionary<KClass<*>, Int> = dictionaryOf()
        get() = field.sref({ this.destinationIndexes = it })
        set(newValue) {
            field = newValue.sref()
        }
    private var backStackState: Dictionary<String, Navigator.BackStackState> = dictionaryOf()
        get() = field.sref({ this.backStackState = it })
        set(newValue) {
            field = newValue.sref()
        }
    private var navigatingToState: Navigator.BackStackState? = BackStackState(route = Companion.rootRoute)
    internal class BackStackState {
        internal val id: String?
        internal val route: String
        internal val destination: ((Any) -> View)?
        internal val targetValue: Any?
        internal val stateSaver: ComposeStateSaver

        internal constructor(id: String? = null, route: String, destination: ((Any) -> View)? = null, targetValue: Any? = null, stateSaver: ComposeStateSaver = ComposeStateSaver()) {
            this.id = id
            this.route = route
            this.destination = destination
            this.targetValue = targetValue.sref()
            this.stateSaver = stateSaver
        }
    }

    internal constructor(navController: NavHostController, destinations: Dictionary<KClass<*>, NavigationDestination>) {
        this.navController = navController
        this.destinations = destinations
        updateDestinationIndexes()
    }

    /// Call with updated state on recompose.
    @Composable
    internal open fun didCompose(navController: NavHostController, destinations: Dictionary<KClass<*>, NavigationDestination>) {
        this.navController = navController
        this.destinations = destinations
        updateDestinationIndexes()
        syncState()
    }

    /// Navigate to a target value specified in a `NavigationLink`.
    internal open fun navigate(to: Any) {
        val targetValue = to
        navigate(to = targetValue, type = type(of = targetValue))
    }

    /// The entry being navigated to.
    internal open fun state(for_: NavBackStackEntry): Navigator.BackStackState? {
        val entry = for_
        return backStackState[entry.id]
    }

    @Composable
    private fun syncState() {
        val entryList = navController.currentBackStack.collectAsState()

        // Fill in ID of state we were navigating to if possible
        navigatingToState?.let { navigatingToState ->
            entryList.value.lastOrNull()?.let { lastEntry ->
                val state = BackStackState(id = lastEntry.id, route = navigatingToState.route, destination = navigatingToState.destination, targetValue = navigatingToState.targetValue, stateSaver = navigatingToState.stateSaver)
                this.navigatingToState = null
                backStackState[lastEntry.id] = state
            }
        }

        // Sync the back stack with remaining states. We delay this to allow views that receive compose calls while animating away to find their state
        LaunchedEffect(entryList.value) {
            delay(1000) // 1 second
            var syncedBackStackState: Dictionary<String, Navigator.BackStackState> = dictionaryOf()
            for (entry in entryList.value.sref()) {
                backStackState[entry.id]?.let { state ->
                    syncedBackStackState[entry.id] = state
                }
            }
            backStackState = syncedBackStackState
        }
    }

    private fun navigate(to: Any, type: KClass<*>?): Boolean {
        val targetValue = to
        if (type == null) {
            return false
        }
        val destination_0 = destinations[type]
        if (destination_0 == null) {
            for (supertype in type.supertypes.sref()) {
                if (navigate(to = targetValue, type = supertype as? KClass<*>)) {
                    return true
                }
            }
            return false
        }
        val route = route(for_ = type, value = targetValue)
        navigatingToState = BackStackState(route = route, destination = destination_0.destination, targetValue = targetValue)
        navController.navigate(route)
        return true
    }

    private fun route(for_: KClass<*>, value: Any): String {
        val targetType = for_
        val index_0 = destinationIndexes[targetType]
        if (index_0 == null) {
            return String(describing = targetType) + "?"
        }
        var valueString: String
        val matchtarget_0 = value as? Identifiable<*>
        if (matchtarget_0 != null) {
            val identifiable = matchtarget_0
            valueString = String(describing = identifiable.id)
        } else {
            val matchtarget_1 = value as? RawRepresentable<*>
            if (matchtarget_1 != null) {
                val rawRepresentable = matchtarget_1
                valueString = String(describing = rawRepresentable.rawValue)
            } else {
                valueString = String(describing = value)
            }
        }
        // Escape '/' because it is meaningful in navigation routes
        valueString = valueString.replacingOccurrences(of = "/", with = "%2F")
        return route(for_ = index_0, valueString = valueString)
    }

    private fun updateDestinationIndexes() {
        for (type in destinations.keys.sref()) {
            if (destinationIndexes[type] == null) {
                destinationIndexes[type] = destinationIndexes.count
            }
        }
    }

    companion object {
        /// Route for the root of the navigation stack.
        internal val rootRoute = "navigationroot"

        /// Number of possible destiation routes.
        ///
        /// We route to destinations by static index rather than a dynamic system based on the provided destination keys because changing the destinations of a `NavHost`
        /// wipes out its back stack. By using a fixed set of indexes, we can maintain the back stack even as we add destination mappings.
        internal val destinationCount = 100

        /// Route for the given destination index and value string.
        internal fun route(for_: Int, valueString: String): String {
            val destinationIndex = for_
            return String(describing = destinationIndex) + "/" + valueString
        }
    }
}

internal val LocalNavigator: ProvidableCompositionLocal<Navigator?> = compositionLocalOf { null as Navigator? }

// Model `NavigationSplitViewStyle` as a struct. Kotlin does not support static members of protocols
class NavigationSplitViewStyle: RawRepresentable<Int> {
    override val rawValue: Int

    constructor(rawValue: Int) {
        this.rawValue = rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is NavigationSplitViewStyle) return false
        return rawValue == other.rawValue
    }

    companion object {

        var automatic = NavigationSplitViewStyle(rawValue = 0)
            get() = field.sref({ this.automatic = it })
            set(newValue) {
                field = newValue.sref()
            }
        var balanced = NavigationSplitViewStyle(rawValue = 1)
            get() = field.sref({ this.balanced = it })
            set(newValue) {
                field = newValue.sref()
            }
        var prominentDetail = NavigationSplitViewStyle(rawValue = 2)
            get() = field.sref({ this.prominentDetail = it })
            set(newValue) {
                field = newValue.sref()
            }
    }
}

class NavigationBarItem: Sendable {
    enum class TitleDisplayMode: Sendable {
        automatic,
        inline,
        large;

        companion object {
        }
    }

    override fun equals(other: Any?): Boolean = other is NavigationBarItem

    override fun hashCode(): Int = "NavigationBarItem".hashCode()

    companion object {
    }
}

internal class NavigationDestinationsPreferenceKey: PreferenceKey<Dictionary<KClass<*>, NavigationDestination>> {

    companion object: PreferenceKeyCompanion<NavigationDestinations> {
        override val defaultValue: Dictionary<KClass<*>, NavigationDestination> = dictionaryOf()
        override fun reduce(value: InOut<Dictionary<KClass<*>, NavigationDestination>>, nextValue: () -> Dictionary<KClass<*>, NavigationDestination>) {
            for ((type, destination) in nextValue()) {
                value.value[type] = destination
            }
        }
    }
}

internal class NavigationTitlePreferenceKey: PreferenceKey<String> {

    companion object: PreferenceKeyCompanion<String> {
        override val defaultValue = ""
        override fun reduce(value: InOut<String>, nextValue: () -> String) {
            value.value = nextValue()
        }
    }
}

class NavigationLink: View, ListItemAdapting {
    internal val value: Any?
    internal val label: View

    constructor(value: Any?, label: () -> View) {
        this.value = value.sref()
        this.label = label()
    }

    constructor(title: String, value: Any?): this(value = value, label = {
        ComposeView { composectx: ComposeContext ->
            Text(title).Compose(composectx)
            ComposeResult.ok
        }
    }) {
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(destination: () -> View, label: () -> View) {
        this.value = null
        this.label = label()
    }

    @Deprecated("This API is not yet available in Skip. Consider filing an issue against the owning library at https://github.com/skiptools, or see the library README for information on adding support", level = DeprecationLevel.ERROR)
    constructor(title: String, destination: () -> View) {
        this.label = EmptyView()
        this.value = null
    }

    @Composable
    override fun ComposeContent(context: ComposeContext) {
        label.Compose(context = context.content(modifier = NavigationModifier(context.modifier)))
    }

    @Composable
    override fun shouldComposeListItem(): Boolean = true

    @Composable
    override fun ComposeListItem(context: ComposeContext, contentModifier: Modifier) {
        Row(modifier = NavigationModifier(modifier = Modifier).then(contentModifier), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1.0f)) {
                // Continue to specialize for list rendering within the NavigationLink (e.g. Label)
                label.Compose(context = context.content(composer = ListItemComposer(contentModifier = Modifier)))
            }
            // Chevron
            androidx.compose.material3.Text("\u203A", color = androidx.compose.ui.graphics.Color.LightGray, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        }
    }

    @Composable
    private fun NavigationModifier(modifier: Modifier): Modifier {
        val navigator = LocalNavigator.current.sref()
        return modifier.clickable(enabled = value != null && EnvironmentValues.shared.isEnabled) {
            if ((value != null) && (navigator != null)) {
                navigator.navigate(to = value)
            }
        }
    }

    companion object {
    }
}

