package com.zhenxiang.langctrl.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun DrawableIcon(
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
