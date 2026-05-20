package com.zhenxiang.langctrl.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zhenxiang.langctrl.R
import com.zhenxiang.langctrl.data.InstalledApp

@Composable
fun AppLanguageControlScreen(viewModel: AppListViewModel) {
    val context = LocalContext.current

    AppLanguageControlContent(
        uiState = viewModel.uiState,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onAppFilterChange = viewModel::updateAppFilter,
        onSupportedAppClick = { packageName ->
            openPerAppLanguageSettings(
                context = context,
                packageName = packageName
            )
        }
    )
}

@Composable
private fun AppLanguageControlContent(
    uiState: AppListUiState,
    onSearchQueryChange: (String) -> Unit,
    onAppFilterChange: (AppFilter) -> Unit,
    onSupportedAppClick: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.consumePointerInput()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Top + WindowInsetsSides.Horizontal
                            )
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.installed_apps),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (uiState.isLoading) {
                            stringResource(id = R.string.loading_apps)
                        } else if (uiState.searchQuery.isNotBlank() || uiState.appFilter != AppFilter.ALL) {
                            stringResource(
                                id = R.string.filtered_apps_found,
                                uiState.apps.size,
                                uiState.totalAppCount
                            )
                        } else {
                            stringResource(id = R.string.apps_found, uiState.apps.size)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        enabled = !uiState.isLoading,
                        singleLine = true,
                        label = {
                            Text(text = stringResource(id = R.string.search))
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.search_placeholder))
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { onSearchQueryChange("") },
                                    enabled = !uiState.isLoading
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_close_24),
                                        contentDescription = stringResource(id = R.string.clear_search)
                                    )
                                }
                            }
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = uiState.appFilter == AppFilter.ALL,
                            onClick = { onAppFilterChange(AppFilter.ALL) },
                            enabled = !uiState.isLoading,
                            label = {
                                Text(text = stringResource(id = R.string.filter_all_apps))
                            }
                        )
                        FilterChip(
                            selected = uiState.appFilter == AppFilter.SUPPORTS_PER_APP_LANGUAGE,
                            onClick = { onAppFilterChange(AppFilter.SUPPORTS_PER_APP_LANGUAGE) },
                            enabled = !uiState.isLoading,
                            label = {
                                Text(text = stringResource(id = R.string.filter_per_app_language))
                            }
                        )
                    }
                }

                HorizontalDivider()
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = innerPadding) {
                items(
                    items = uiState.apps,
                    key = { it.packageName }
                ) { app ->
                    AppListItem(
                        app = app,
                        onClick = { onSupportedAppClick(app.packageName) }
                    )
                    Divider(modifier = Modifier.padding(start = 84.dp))
                }

                item {
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                }
            }
        }
    }
}

private fun Modifier.consumePointerInput(): Modifier {
    return pointerInput(Unit) {}
}

@Composable
private fun AppListItem(
    app: InstalledApp,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = app.supportsPerAppLanguage,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DrawableIcon(
            drawable = app.icon,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = app.appName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = app.packageName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.app_version, app.versionName, app.versionCode),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!app.supportsPerAppLanguage) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.per_app_language_not_supported),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun DrawableIcon(
    drawable: Drawable,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        drawIntoCanvas { canvas ->
            drawable.setBounds(0, 0, size.width.toInt(), size.height.toInt())
            drawable.draw(canvas.nativeCanvas)
        }
    }
}

private fun openPerAppLanguageSettings(context: Context, packageName: String) {
    val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS)
        .setData(Uri.parse("package:$packageName"))

    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(
            context,
            context.getString(R.string.per_app_language_settings_unavailable),
            Toast.LENGTH_SHORT
        ).show()
    }
}
