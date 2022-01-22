package com.example.downloadfiles.presentation.core

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isWritePermissionAllowed(context: Context): Boolean = ContextCompat.checkSelfPermission(
    context,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
) == PackageManager.PERMISSION_GRANTED
