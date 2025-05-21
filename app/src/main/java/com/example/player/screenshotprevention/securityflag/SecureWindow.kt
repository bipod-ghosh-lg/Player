package com.example.player.screenshotprevention.securityflag

import android.view.Window
import android.view.WindowManager

object SecureWindow {
    fun preventScreenshots(window: Window) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
}
