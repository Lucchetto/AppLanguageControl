package com.zhenxiang.langctrl.data

import android.app.LocaleConfig
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class InstalledAppsRepository(
    private val context: Context
) {
    private val packageManager = context.packageManager

    fun loadInstalledApps(): List<InstalledApp> {
        return packageManager
            .getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
            .mapNotNull { packageInfo ->
                val applicationInfo = packageInfo.applicationInfo ?: return@mapNotNull null
                InstalledApp(
                    appName = applicationInfo.loadLabel(packageManager).toString(),
                    packageName = packageInfo.packageName,
                    versionName = packageInfo.versionName ?: "unknown",
                    versionCode = packageInfo.longVersionCode,
                    icon = applicationInfo.loadIcon(packageManager),
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
