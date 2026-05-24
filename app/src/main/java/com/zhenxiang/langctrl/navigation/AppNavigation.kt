package com.zhenxiang.langctrl.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.zhenxiang.langctrl.ui.AppLanguageControlScreen
import com.zhenxiang.langctrl.ui.AppListViewModel

@Composable
fun AppNavigation(viewModel: AppListViewModel) {
    val navController = rememberNavController(AppDestination.Home)

    NavDisplay(
        backStack = navController.backStack,
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry(AppDestination.Home) {
                AppLanguageControlScreen(viewModel = viewModel)
            }
        },
        onBack = {}
    )
}
