package com.example.downloadfiles.domain.gateway

import com.example.downloadfiles.entity.remotefiles.FilesRemoteResponse
import retrofit2.http.GET


interface FilesApi {
    @GET("getListOfFilesResponse.json")
    suspend fun getFiles():List<FilesRemoteResponse>
}