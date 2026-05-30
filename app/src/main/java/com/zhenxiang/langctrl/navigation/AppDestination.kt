package com.zhenxiang.langctrl.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class AppDestination : NavKey {

    @Serializable
    data object Home : AppDestination()

    @Serializable
    data object About : AppDestination()

    @Serializable
    data object License : AppDestination()
}
