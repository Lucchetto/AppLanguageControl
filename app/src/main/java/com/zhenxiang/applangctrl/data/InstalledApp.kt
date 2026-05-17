package com.zhenxiang.applangctrl.data

import android.graphics.drawable.Drawable

data class InstalledApp(
    val appName: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val icon: Drawable,
    val supportsPerAppLanguage: Boolean
)
