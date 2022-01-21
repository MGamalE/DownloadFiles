package com.example.downloadfiles.presentation.core

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File

object DownloadFilesManager {
    fun downloadFile(
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
        val uri = Uri.parse(url?.trim())
        try {
            val request = DownloadManager.Request(uri)
            request.setDestinationInExternalPublicDir(
                "/MyDownloadedFiles",
                fileType + System.currentTimeMillis() + extension
            )
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setTitle(title)

            Toast.makeText(activity, "Start Downloading..", Toast.LENGTH_SHORT).show()

            downloadReference = dm.enqueue(request) ?: 0


        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Toast.makeText(activity, "Invalid URL..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Something went wrong, try again..", Toast.LENGTH_SHORT).show()
        }

        //Register broadcast receiver
        activity.registerReceiver(
            broadcastReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        return downloadReference
    }
}