package com.example.downloadfiles.presentation.features.fileslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.downloadfiles.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilesListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}