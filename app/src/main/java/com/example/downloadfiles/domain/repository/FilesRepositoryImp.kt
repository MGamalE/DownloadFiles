package com.example.downloadfiles.domain.repository

import com.example.downloadfiles.domain.gateway.FilesApi
import com.example.downloadfiles.entity.remotefiles.FilesRemoteResponse
import kotlinx.coroutines.flow.Flow

class FilesRepositoryImp(server:FilesApi):FilesRepository {
    override suspend fun getFiles(): Flow<List<FilesRemoteResponse>> {
        TODO("Not yet implemented")
    }
}