package com.rip.shrimptank.utils

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.rip.shrimptank.R
import com.rip.shrimptank.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.TimeUnit

object UserInteractions {

    var userId: String = ""
    var userData: User? = null
    private var dlg: AlertDialog? = null

    fun chkNet(ctx: Context): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    fun timeAgo(ts: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - ts

        val sec = TimeUnit.MILLISECONDS.toSeconds(diff)
        val min = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hr = TimeUnit.MILLISECONDS.toHours(diff)
        val day = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            sec < 60 -> "Just now"
            min < 60 -> "$min minutes ago"
            hr < 24 -> "$hr hours ago"
            day < 7 -> "$day days ago"
            day < 30 -> "${day / 7} weeks ago"
            else -> "${day / 30} months ago"
        }
    }

    fun showDlg(ctx: Context, msg: String) {
        MaterialAlertDialogBuilder(ctx)
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    fun showDlg(ctx: Context, msg: String, listener: DialogInterface.OnClickListener) {
        MaterialAlertDialogBuilder(ctx)
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("Ok", listener)
            .create()
            .show()
    }

    fun showLoading(ctx: Context) {
        if (dlg == null) {
            val builder = MaterialAlertDialogBuilder(ctx)
            val layout : View = LayoutInflater.from(ctx).inflate(R.layout.loading, null)
            builder.setView(layout)
            builder.setCancelable(false)
            dlg = builder.create()
            dlg?.show()
        }
    }

    fun hideLoad() {
        dlg?.let { dialog ->
            if (dialog.isShowing) {
                try {
                    dialog.dismiss()
                } catch (e: IllegalArgumentException) {
                    // The dialog's view is not attached to the window manager.
                    e.printStackTrace()
                }
            }
        }
        dlg = null
    }
}
