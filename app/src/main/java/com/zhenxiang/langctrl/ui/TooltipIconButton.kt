package com.zhenxiang.langctrl.ui

import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics

@Composable
fun TooltipIconButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) = TooltipBox(
    positionProvider =
        TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
        ),
    tooltip = {
        PlainTooltip(
            modifier =
                Modifier.semantics {
                    // TODO(b/496338253): Remove this modifier once bug where
                    //   tooltip text is not announced by a11y screen readers
                    //   is resolved.
                    liveRegion = LiveRegionMode.Assertive
                    paneTitle = contentDescription
                }
        ) {
            Text(contentDescription)
        }
    },
    state = rememberTooltipState(),
    modifier = modifier,
) {
    IconButton(onClick, enabled = enabled, content = content)
}