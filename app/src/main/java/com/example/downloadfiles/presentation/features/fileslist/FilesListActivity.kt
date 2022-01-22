package com.example.downloadfiles.presentation.features.fileslist

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.downloadfiles.R
import com.example.downloadfiles.databinding.ActivityMainBinding
import com.example.downloadfiles.entity.uifiles.DownloadStatus
import com.example.downloadfiles.entity.uifiles.FilesUiData
import com.example.downloadfiles.presentation.core.DownloadFilesManager
import com.example.downloadfiles.presentation.core.isWritePermissionAllowed
import com.example.downloadfiles.presentation.core.onSnack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val PERMISSION_REQUEST_CODE = 0

@AndroidEntryPoint
class FilesListActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private val viewModel: FilesListViewModel by viewModels()

    private lateinit var adapter: FilesListAdapter

    //Hold the download id
    private var downloadReference: Long = 0

    //Hold an item position of each file in the files list
    private var position = 0

    //Hold file model data
    private var file: FilesUiData = FilesUiData()

    /**
     * Listen to broadcast fired actions in case of downloading file is completed
     */
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadReference) {
                onSnack(binding?.root!!, getString(R.string.download_complete))
                adapter.updateFiles(
                    FilesUiData(
                        id = file.id,
                        name = file.name,
                        type = file.type,
                        url = file.url,
                        status = DownloadStatus.COMPLETED,
                        position = file.position
                    ), position
                )

            }
        }
    }

    /**
     * Receive the callback of accessing write permission of android >= 11
     */
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        // perform action when allow permission success
                        startFileDownloading(
                            this,
                            file.url, file.name, file.type
                        )

                    } else {
                        Toast.makeText(
                            this,
                            "Allow permission for storage access!",
                            Toast.LENGTH_SHORT
                        )
                            .show();
                    }
                }
            }
        }

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

    /**
     * This method to initialize [FilesListAdapter], then inflate recyclerview
     */
    private fun inflateFilesRecyclerView() {
        adapter = FilesListAdapter({ fileData ->
            file = FilesUiData(
                id = fileData.id, name = fileData.name, type = fileData.type,
                url = fileData.url, status = DownloadStatus.PROGRESS, position = fileData.position
            )
            position = fileData.position
            checkWriteRunPermission(this, fileData.url, fileData.name.toString(), fileData)
        }, mutableListOf())

        binding?.rvFiles?.adapter = adapter
    }


    /**
     * This method to check for runtime permission before downloading files
     */
    private fun checkWriteRunPermission(
        activity: FilesListActivity,
        url: String?,
        name: String,
        fileData: FilesUiData
    ) {
        /**
         * Check if current android sdk above 30
         */
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                startFileDownloading(activity, url, name, fileData.type)
            } else {
                requestStorageAccessForAndroid11()
            }
        } else {
            if (activity.isWritePermissionAllowed(activity)) {
                startFileDownloading(activity, url, name, fileData.type)
            } else {
                requestPermissions(
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    /**
     * Request write permission for scoped storage in android 11 and above
     */
    private fun requestStorageAccessForAndroid11() {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data =
                Uri.parse(String.format("package:%s", applicationContext.packageName))
            resultLauncher.launch(intent)
        } catch (e: Exception) {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            resultLauncher.launch(intent)
        }
    }

    /**
     * This method to listen for the broadcast of downloading files complete status
     */
    private fun startFileDownloading(
        mainActivity: FilesListActivity,
        url: String?,
        fileName: String?,
        fileData: String?
    ) {
        downloadReference = DownloadFilesManager.downloadFile(
            binding?.root!!,
            mainActivity,
            url,
            fileName,
            fileData,
            broadcastReceiver
        )
    }



    /**
     * Receive the callback of accessing write permission of android < 10
     */
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
                    startFileDownloading(this, file.url, file.name, file.type)
                } else {
                    onSnack(binding?.root!!, getString(R.string.error_permission))
                }
                return
            }

        }
    }

}