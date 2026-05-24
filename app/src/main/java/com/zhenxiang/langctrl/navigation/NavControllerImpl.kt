package com.zhenxiang.langctrl.navigation

import androidx.navigation3.runtime.NavKey

internal class NavControllerImpl<T : NavKey>(
    override val backStack: MutableList<T>,
): NavController<T> {

    override fun navigate(destination: T) {
        backStack += destination
    }

    override fun popBackStack() {
        backStack.removeAt(backStack.lastIndex)
    }
}
