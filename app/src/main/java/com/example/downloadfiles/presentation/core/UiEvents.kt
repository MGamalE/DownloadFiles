package com.example.downloadfiles.presentation.core

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

fun onSnack(view: View, message:String) {
    val snackBar = Snackbar.make(
        view, message,
        Snackbar.LENGTH_LONG
    ).setAction("Action", null)
    snackBar.setActionTextColor(Color.WHITE)
    val snackBarView = snackBar.view
    snackBarView.setBackgroundColor(Color.BLACK)
    val textView =
        snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(Color.WHITE)
    textView.textSize = 14f
    snackBar.show()
}
