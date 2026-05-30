package com.zhenxiang.langctrl

import android.content.Intent
import androidx.core.net.toUri

object IntentUtils {

    fun openStringUriIntent(string: String) = Intent(Intent.ACTION_VIEW).apply {
        data = string.toUri()
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
