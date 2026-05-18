package com.zhenxiang.langctrl.ui

import com.zhenxiang.langctrl.data.InstalledApp

data class AppListUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val appFilter: AppFilter = AppFilter.ALL,
    val totalAppCount: Int = 0,
    val apps: List<InstalledApp> = emptyList()
)

enum class AppFilter {
    ALL,
    SUPPORTS_PER_APP_LANGUAGE
}
