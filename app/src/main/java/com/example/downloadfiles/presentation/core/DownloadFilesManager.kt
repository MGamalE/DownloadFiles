package com.example.downloadfiles.presentation.core

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.Toast
import com.example.downloadfiles.R
import java.io.File

object DownloadFilesManager {
    fun downloadFile(
        view:View,
        activity: Context,
        url: String?,
        title: String?,
        fileType: String?,
        broadcastReceiver: BroadcastReceiver
    ): Long {
        var downloadReference: Long = 0
        val direct = File(
            Environment.getExternalStorageDirectory().toString() +
                    "/MyDownloadedFiles"
        )

        if (!direct.exists()) {
            direct.mkdirs()
        }
        val extension = url?.substring(url.lastIndexOf("."))
        val dm: DownloadManager =
            activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url?.trim()?.substring(url.lastIndexOf('(') +1))
        try {
            val request = DownloadManager.Request(uri)
            request.setDestinationInExternalPublicDir(
                "/MyDownloadedFiles",
                fileType + System.currentTimeMillis() + extension
            )
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setTitle(title)

            onSnack(view,activity.getString(R.string.downloading_start))

            downloadReference = dm.enqueue(request) ?: 0

            //Register broadcast receiver
            activity.registerReceiver(
                broadcastReceiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )

        }catch (e: Exception) {
            e.printStackTrace()
            onSnack(view,activity.getString(R.string.error_downloading))
        }

        return downloadReference
    }
}