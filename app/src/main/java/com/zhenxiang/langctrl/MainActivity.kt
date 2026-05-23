package com.zhenxiang.langctrl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.zhenxiang.langctrl.navigation.AppNavigation
import com.zhenxiang.langctrl.ui.AppListViewModel
import com.zhenxiang.langctrl.ui.theme.AppLanguageControlTheme

class MainActivity : ComponentActivity() {
    private val viewModel: AppListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppLanguageControlTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
