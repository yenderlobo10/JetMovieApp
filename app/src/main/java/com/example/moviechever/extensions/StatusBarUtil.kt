package com.example.moviechever.extensions

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.children


fun Activity.applyStatusBarTransparent(isLightMode: Boolean = false) {

    setTransparentStatusBar(this)
    setRootView(this)

    if (isLightMode) applyStatusBarLightMode()
}

@Suppress("DEPRECATION")
fun Activity.applyStatusBarLightMode() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        this.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

@Suppress("DEPRECATION")
fun Activity.applyStatusBarDarkMode() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

//#region Private methods

@Suppress("DEPRECATION")
private fun setTransparentStatusBar(activity: Activity) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        activity.window.statusBarColor = Color.TRANSPARENT

    } else {

        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

private fun setRootView(activity: Activity) {

    val parent = activity.findViewById<View>(android.R.id.content) as ViewGroup

    parent.children.forEach {
        if (it is ViewGroup) {
            it.setFitsSystemWindows(true)
            it.clipToPadding = true
        }
    }
}

//#endregion
