package com.example.downloadfiles.domain.repository

import com.example.downloadfiles.domain.gateway.FilesApi
import com.example.downloadfiles.entity.remotefiles.FilesRemoteResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FilesRepositoryImp(private val server: FilesApi) : FilesRepository {

    /**
     * Request files from gateway, then emit to [Flow]
     */
    override suspend fun getFiles(): Flow<List<FilesRemoteResponse>> = flow {
        val response = server.getFiles()
        emit(response)
    }
}