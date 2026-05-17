package com.zhenxiang.applangctrl.ui

import com.zhenxiang.applangctrl.data.InstalledApp

data class AppListUiState(
    val isLoading: Boolean = true,
    val apps: List<InstalledApp> = emptyList()
)
