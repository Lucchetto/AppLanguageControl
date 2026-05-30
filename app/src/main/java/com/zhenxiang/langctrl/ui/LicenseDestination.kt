package com.zhenxiang.langctrl.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zhenxiang.langctrl.navigation.AppDestination
import com.zhenxiang.langctrl.navigation.NavController

@Composable
fun LicenseDestination(
    navController: NavController<AppDestination>
) {
    val licenseViewModel: LicenseViewModel = viewModel()
    val uiState by licenseViewModel.uiState.collectAsStateWithLifecycle()

    LicenseScreen(
        uiState = uiState,
        onBack = navController::popBackStack,
    )
}