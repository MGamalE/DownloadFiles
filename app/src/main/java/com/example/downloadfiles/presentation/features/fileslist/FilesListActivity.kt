package com.example.downloadfiles.presentation.features.fileslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.downloadfiles.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FilesListActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private val viewModel: FilesListViewModel by viewModels()

    private lateinit var adapter: FilesListAdapter

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
        adapter = FilesListAdapter({}, mutableListOf())

        binding?.rvFiles?.adapter = adapter
    }


}