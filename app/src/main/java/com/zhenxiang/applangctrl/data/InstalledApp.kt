package com.zhenxiang.applangctrl.data

import android.graphics.Bitmap

data class InstalledApp(
    val appName: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val icon: Bitmap,
    val supportsPerAppLanguage: Boolean
)
