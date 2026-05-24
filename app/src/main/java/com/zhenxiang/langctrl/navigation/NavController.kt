package com.zhenxiang.langctrl.navigation

import androidx.navigation3.runtime.NavKey

interface NavController<T: NavKey> {

    val backStack: List<T>

    fun navigate(destination: T)

    fun popBackStack()
}
