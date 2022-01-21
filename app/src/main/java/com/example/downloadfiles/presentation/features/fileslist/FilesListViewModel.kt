package com.example.downloadfiles.presentation.features.fileslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.downloadfiles.domain.repository.FilesRepository
import com.example.downloadfiles.entity.uifiles.FilesUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FilesListViewModel(private val repository: FilesRepository):ViewModel() {
    private val _result = MutableStateFlow<List<FilesUiData>>(emptyList())
    val result: StateFlow<List<FilesUiData>> = _result

    fun requestListOfFiles(){
        viewModelScope.launch {
            repository.getFiles().collectLatest {
            }
        }
    }

}