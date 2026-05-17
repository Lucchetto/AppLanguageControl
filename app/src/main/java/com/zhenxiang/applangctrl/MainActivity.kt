package com.zhenxiang.applangctrl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.zhenxiang.applangctrl.ui.AppLanguageControlScreen
import com.zhenxiang.applangctrl.ui.AppListViewModel
import com.zhenxiang.applangctrl.ui.theme.AppLanguageControlTheme

class MainActivity : ComponentActivity() {
    private val viewModel: AppListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppLanguageControlTheme {
                AppLanguageControlScreen(viewModel = viewModel)
            }
        }
    }
}
