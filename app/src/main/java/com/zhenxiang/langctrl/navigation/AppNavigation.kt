package com.zhenxiang.langctrl.navigation

import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.zhenxiang.langctrl.LicenseDestination
import com.zhenxiang.langctrl.ui.AboutDialog
import com.zhenxiang.langctrl.ui.AppLanguageControlScreen
import com.zhenxiang.langctrl.ui.AppListViewModel
import com.zhenxiang.langctrl.ui.LicenseViewModel
import com.zhenxiang.langctrl.ui.MaterialNavigationAnimation

@Composable
fun AppNavigation(viewModel: AppListViewModel) {
    val navController = rememberNavController<AppDestination>(AppDestination.Home)

    NavDisplay(
        backStack = navController.backStack,
        onBack = navController::popBackStack,
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategies = remember {
            listOf(DialogSceneStrategy(), SinglePaneSceneStrategy())
        },
        transitionSpec = {
            MaterialNavigationAnimation.enterTransition.invoke(this) togetherWith
                    MaterialNavigationAnimation.exitTransition.invoke(this)
        },
        popTransitionSpec = {
            MaterialNavigationAnimation.popEnterTransition.invoke(this) togetherWith
                    MaterialNavigationAnimation.popExitTransition.invoke(this)
        },
        entryProvider = entryProvider {
            entry(AppDestination.Home) {
                AppLanguageControlScreen(viewModel, navController)
            }
            entry(
                AppDestination.About,
                metadata = DialogSceneStrategy.dialog()
            ) {
                AboutDialog(navController)
            }
            entry(AppDestination.License) {
                LicenseDestination(navController)
            }
        },
    )
}

