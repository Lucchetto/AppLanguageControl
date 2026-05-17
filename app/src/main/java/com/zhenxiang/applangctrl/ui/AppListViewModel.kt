package com.zhenxiang.applangctrl.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zhenxiang.applangctrl.data.InstalledApp
import com.zhenxiang.applangctrl.data.InstalledAppsRepository
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
                apps = installedApps.filterBy(uiState.searchQuery)
            )
        }
    }

    fun updateSearchQuery(query: String) {
        uiState = uiState.copy(
            searchQuery = query,
            apps = installedApps.filterBy(query)
        )
    }
}

private fun List<InstalledApp>.filterBy(query: String): List<InstalledApp> {
    val normalizedQuery = query.trim()
    if (normalizedQuery.isEmpty()) {
        return this
    }

    return filter { app ->
        app.appName.contains(normalizedQuery, ignoreCase = true) ||
            app.packageName.contains(normalizedQuery, ignoreCase = true)
    }
}
