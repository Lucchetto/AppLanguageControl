@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.zhenxiang.langctrl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.internal.ProvideContentColorTextStyle
import androidx.compose.material3.shouldUsePrecisionPointerComponentSizing
import androidx.compose.material3.tokens.DialogTokens
import androidx.compose.material3.value
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.zhenxiang.langctrl.BuildConfig
import com.zhenxiang.langctrl.IntentUtils
import com.zhenxiang.langctrl.R
import com.zhenxiang.langctrl.navigation.AppDestination
import com.zhenxiang.langctrl.navigation.NavController
import com.zhenxiang.langctrl.ui.theme.AppLanguageControlTheme

@Composable
fun AboutDialog(navController: NavController<AppDestination>) = AboutDialogContent(
    onDismissRequest = navController::popBackStack,
    onOpenLicense = {
        navController.popBackStack()
        navController.navigate(AppDestination.License)
    }
)

private val IconPadding = PaddingValues(bottom = 16.dp)
private val TitlePadding = PaddingValues(bottom = 16.dp)

@Composable
internal fun AlertDialogContent(
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)?,
    title: (@Composable () -> Unit)?,
    text: @Composable (() -> Unit)?,
    shape: Shape,
    containerColor: Color,
    tonalElevation: Dp,
    buttonContentColor: Color,
    iconContentColor: Color,
    titleContentColor: Color,
    textContentColor: Color,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        tonalElevation = tonalElevation,
    ) {
        Column(
            modifier = Modifier.padding(AlertDialogDefaults.dialogPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            icon?.let {
                CompositionLocalProvider(LocalContentColor provides iconContentColor) {
                    Box(Modifier.padding(IconPadding).align(Alignment.CenterHorizontally)) {
                        icon()
                    }
                }
            }
            title?.let {
                val textStyle =
                    if (shouldUsePrecisionPointerComponentSizing.value) {
                        MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 20.sp,
                            lineHeight = 26.sp,
                        )
                    } else {
                        DialogTokens.HeadlineFont.value
                    }
                ProvideContentColorTextStyle(
                    contentColor = titleContentColor,
                    textStyle = textStyle,
                ) {
                    Box(
                        // Align the title to the center when an icon is present.
                        Modifier.padding(TitlePadding)
                            .align(
                                if (icon == null) {
                                    Alignment.Start
                                } else {
                                    Alignment.CenterHorizontally
                                }
                            )
                    ) {
                        title()
                    }
                }
            }
            text?.let {
                val textStyle = DialogTokens.SupportingTextFont.value
                ProvideContentColorTextStyle(
                    contentColor = textContentColor,
                    textStyle = textStyle,
                ) {
                    Box(
                        Modifier.weight(weight = 1f, fill = false)
                            .padding(AlertDialogDefaults.textPadding)
                    ) {
                        text()
                    }
                }
            }
            Box(modifier = Modifier.align(Alignment.End)) {
                val textStyle = DialogTokens.ActionLabelTextFont.value
                ProvideContentColorTextStyle(
                    contentColor = buttonContentColor,
                    textStyle = textStyle,
                    content = buttons,
                )
            }
        }
    }
}

@Composable
fun AboutDialogContent(
    onDismissRequest: () -> Unit,
    onOpenLicense: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AlertDialogContent(
        {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.close))
            }
        },
        modifier,
        {
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
                   Text(
                       stringResource(R.string.developed_by),
                       style = MaterialTheme.typography.bodySmall,
                   )
               }
               Row(
                   modifier = Modifier.padding(top = 8.dp)
               ) {
                   TooltipIconButton(
                       onClick = {
                           val intent = IntentUtils.openStringUriIntent(
                               "https://github.com/Lucchetto/AppLanguageControl"
                           )

                           if (intent.resolveActivity(context.packageManager) != null) {
                               context.startActivity(intent)
                           }
                       },
                       contentDescription = stringResource(R.string.open_github),
                   ) {
                       Icon(
                           painterResource(R.drawable.ic_github_24),
                           contentDescription = stringResource(R.string.open_github),
                       )
                   }
                   TooltipIconButton(
                       onClick = onOpenLicense,
                       contentDescription = stringResource(R.string.open_license)
                   ) {
                       Icon(
                           painterResource(R.drawable.ic_balance_24),
                           contentDescription = stringResource(R.string.open_license),
                       )
                   }
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
}

@Composable
@Preview
fun AboutDialogContentPreview() = AppLanguageControlTheme {
    AboutDialogContent(onDismissRequest = {}, onOpenLicense = {})
}