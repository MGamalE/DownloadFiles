package com.example.downloadfiles.domain.repository

import com.example.downloadfiles.entity.remotefiles.FilesRemoteResponse
import kotlinx.coroutines.flow.Flow

interface FilesRepository {
    suspend fun getFiles(): Flow<List<FilesRemoteResponse>>
}