package com.zhenxiang.langctrl.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

@Composable
@Suppress("UNCHECKED_CAST")
fun <T : NavKey> rememberNavController(initial: T): NavController<T> {
    val backStack = rememberNavBackStack(initial) as NavBackStack<T>
    return remember { NavControllerImpl(backStack) }
}
