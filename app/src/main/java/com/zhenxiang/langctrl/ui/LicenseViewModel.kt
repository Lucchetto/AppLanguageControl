package com.zhenxiang.langctrl.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LicenseViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LicenseUiState())
    val uiState: StateFlow<LicenseUiState> = _uiState.asStateFlow()

    init {
        loadLicense()
    }

    fun loadLicense() {
        _uiState.value = LicenseUiState(isLoading = true)
        viewModelScope.launch {
            val text = withContext(Dispatchers.IO) {
                try {
                    getApplication<Application>()
                        .assets
                        .open("LICENSE.md")
                        .bufferedReader()
                        .use { it.readText() }
                } catch (e: Exception) {
                    "Failed to load license text."
                }
            }
            _uiState.value = LicenseUiState(isLoading = false, text = text)
        }
    }
}
