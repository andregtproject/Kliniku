package com.kliniku.official.auth.util

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.kliniku.official.R

@SuppressLint("ObsoleteSdkInt")
object LoadingUtils {

    fun showLoading(window: Window, overlay: View) {
        // Ganti status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            @Suppress("DEPRECATION")
            window.statusBarColor = ContextCompat.getColor(overlay.context, R.color.colorOverlay)
        }

        overlay.visibility = View.VISIBLE
        overlay.bringToFront()

        // Disable interaksi user
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideLoading(window: Window, overlay: View) {
        // Kembalikan status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            @Suppress("DEPRECATION")
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }

        overlay.visibility = View.GONE

        // Enable kembali interaksi
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}
