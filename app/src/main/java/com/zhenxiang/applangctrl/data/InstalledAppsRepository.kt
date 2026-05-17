package com.zhenxiang.applangctrl.data

import android.app.LocaleConfig
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

class InstalledAppsRepository(
    private val context: Context
) {
    private val packageManager = context.packageManager

    fun loadInstalledApps(): List<InstalledApp> {
        val iconSize = (48 * context.resources.displayMetrics.density).toInt().coerceAtLeast(48)

        return packageManager
            .getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
            .mapNotNull { packageInfo ->
                val applicationInfo = packageInfo.applicationInfo ?: return@mapNotNull null
                InstalledApp(
                    appName = applicationInfo.loadLabel(packageManager).toString(),
                    packageName = packageInfo.packageName,
                    versionName = packageInfo.versionName ?: "unknown",
                    versionCode = packageInfo.longVersionCode,
                    icon = applicationInfo.loadIcon(packageManager).toBitmap(iconSize),
                    supportsPerAppLanguage = supportsPerAppLanguage(packageInfo)
                )
            }
            .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.appName })
    }

    private fun supportsPerAppLanguage(packageInfo: PackageInfo): Boolean {
        return try {
            val packageContext = context.createPackageContext(packageInfo.packageName, 0)
            val localeConfig = LocaleConfig(packageContext)
            localeConfig.status == LocaleConfig.STATUS_SUCCESS &&
                localeConfig.supportedLocales?.isEmpty == false
        } catch (_: PackageManager.NameNotFoundException) {
            false
        } catch (_: SecurityException) {
            false
        } catch (_: RuntimeException) {
            false
        }
    }
}

private fun Drawable.toBitmap(sizePx: Int): Bitmap {
    if (this is BitmapDrawable && bitmap != null) {
        return bitmap
    }

    val width = if (intrinsicWidth > 0) intrinsicWidth else sizePx
    val height = if (intrinsicHeight > 0) intrinsicHeight else sizePx
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { bitmap ->
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
    }
}
