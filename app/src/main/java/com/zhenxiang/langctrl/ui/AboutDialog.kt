@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.zhenxiang.langctrl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialogContent
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.tokens.DialogTokens
import androidx.compose.material3.value
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.zhenxiang.langctrl.BuildConfig
import com.zhenxiang.langctrl.R
import com.zhenxiang.langctrl.navigation.AppDestination
import com.zhenxiang.langctrl.navigation.NavController
import com.zhenxiang.langctrl.ui.theme.AppLanguageControlTheme

@Composable
fun AboutDialog(navController: NavController<AppDestination>) = AboutDialogContent(
    navController::popBackStack
)

@Composable
fun AboutDialogContent(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) = AlertDialogContent(
    {
        TextButton(onClick = onDismissRequest) {
            Text(stringResource(R.string.close))
        }
    },
    modifier,
    {
        val context = LocalContext.current
        val icon = remember {
            requireNotNull(
                context.packageManager?.getApplicationIcon(context.applicationInfo) ?: ContextCompat.getDrawable(context, R.drawable.ic_info_24)
            )
        }

        DrawableIcon(icon, Modifier.size(48.dp))
    },
    { Text(stringResource(R.string.about_app_title)) },
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            ) {
                Text(
                    stringResource(
                        id = R.string.app_version,
                        BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE
                    )
                )
            }
        }
    },
    shape = AlertDialogDefaults.shape,
    containerColor = AlertDialogDefaults.containerColor,
    buttonContentColor = DialogTokens.ActionLabelTextColor.value,
    tonalElevation = AlertDialogDefaults.TonalElevation,
    iconContentColor = AlertDialogDefaults.iconContentColor,
    titleContentColor = AlertDialogDefaults.titleContentColor,
    textContentColor = AlertDialogDefaults.textContentColor,
)

@Composable
@Preview
fun AboutDialogContentPreview() = AppLanguageControlTheme {
    AboutDialogContent({})
}