package com.zhenxiang.langctrl.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zhenxiang.langctrl.data.InstalledApp
import com.zhenxiang.langctrl.data.InstalledAppsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = InstalledAppsRepository(application.applicationContext)
    private var installedApps: List<InstalledApp> = emptyList()

    var uiState by mutableStateOf(AppListUiState())
        private set

    init {
        loadApps()
    }

    fun loadApps() {
        uiState = uiState.copy(isLoading = true)
        viewModelScope.launch {
            val apps = withContext(Dispatchers.IO) {
                repository.loadInstalledApps()
            }
            installedApps = apps
            uiState = uiState.copy(
                isLoading = false,
                totalAppCount = installedApps.size,
                apps = installedApps.filterBy(
                    query = uiState.searchQuery,
                    appFilter = uiState.appFilter
                )
            )
        }
    }

    fun updateSearchQuery(query: String) {
        uiState = uiState.copy(
            searchQuery = query,
            apps = installedApps.filterBy(
                query = query,
                appFilter = uiState.appFilter
            )
        )
    }

    fun updateAppFilter(appFilter: AppFilter) {
        uiState = uiState.copy(
            appFilter = appFilter,
            apps = installedApps.filterBy(
                query = uiState.searchQuery,
                appFilter = appFilter
            )
        )
    }
}

private fun List<InstalledApp>.filterBy(
    query: String,
    appFilter: AppFilter
): List<InstalledApp> {
    val normalizedQuery = query.trim()

    return filter { app ->
        val matchesAppFilter = when (appFilter) {
            AppFilter.ALL -> true
            AppFilter.SUPPORTS_PER_APP_LANGUAGE -> app.supportsPerAppLanguage
        }
        val matchesQuery = normalizedQuery.isEmpty() ||
            app.appName.contains(normalizedQuery, ignoreCase = true) ||
            app.packageName.contains(normalizedQuery, ignoreCase = true)

        matchesAppFilter && matchesQuery
    }
}
