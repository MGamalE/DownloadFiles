package com.example.downloadfiles.presentation.features.fileslist

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.downloadfiles.databinding.ActivityMainBinding
import com.example.downloadfiles.entity.uifiles.DownloadStatus
import com.example.downloadfiles.entity.uifiles.FilesUiData
import com.example.downloadfiles.presentation.core.DownloadFilesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

const val PERMISSION_REQUEST_CODE = 0

@AndroidEntryPoint
class FilesListActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private val viewModel: FilesListViewModel by viewModels()

    private lateinit var adapter: FilesListAdapter

    private var downloadReference: Long = 0
    private var file: FilesUiData = FilesUiData()
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        triggerRefreshUi()

        inflateFilesRecyclerView()

        observeRequestGetListOfFiles()

    }

    private fun triggerRefreshUi() {
        binding?.swipeRefresh?.setOnRefreshListener {
            binding?.swipeRefresh?.isRefreshing = false
        }
    }


    private fun observeRequestGetListOfFiles() {
        /**
         * Start loading
         */
        binding?.swipeRefresh?.isRefreshing = true

        /**
         * Request files gateway
         */
        viewModel.requestListOfFiles()

        /**
         * Observe files gateway request
         */
        lifecycleScope.launch {
            viewModel.result.collect {
                binding?.swipeRefresh?.isRefreshing = false
                adapter.insertNewItems(it)
            }
        }
    }

    private fun inflateFilesRecyclerView() {
        adapter = FilesListAdapter({ fileData->
            file = FilesUiData(
                id = fileData.id, name = fileData.name, type = fileData.type,
                url = fileData.url, status = DownloadStatus.PROGRESS, position = fileData.position
            )
            position = fileData.position
            requestFileToDownload(this, fileData.url, fileData.name.toString(), fileData)
        }, mutableListOf())

        binding?.rvFiles?.adapter = adapter
    }

    private fun requestFileToDownload(
        activity: FilesListActivity,
        url: String?,
        name: String,
        fileData: FilesUiData
    ) {
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //startdownloadingfile
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun startFileDownloading(
        mainActivity: FilesListActivity,
        url: String?,
        fileName: String?,
        fileData: String?
    ) {

        downloadReference = DownloadFilesManager.downloadFile(
            mainActivity,
            url,
            fileName,
            fileData
        )

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    //startdownloadingfile

                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
                }
                return
            }

        }
    }

}