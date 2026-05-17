package com.zhenxiang.applangctrl.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zhenxiang.applangctrl.data.InstalledAppsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = InstalledAppsRepository(application.applicationContext)

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
            uiState = AppListUiState(
                isLoading = false,
                apps = apps
            )
        }
    }
}
