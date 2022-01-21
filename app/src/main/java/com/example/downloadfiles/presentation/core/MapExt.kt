package com.example.downloadfiles.presentation.core

import com.example.downloadfiles.entity.remotefiles.FilesRemoteResponse
import com.example.downloadfiles.entity.uifiles.FilesUiData

/**
 * Mapper to map [FilesRemoteResponse] to [FilesUiData].
 */
fun FilesRemoteResponse.mapToUiModel(): FilesUiData = FilesUiData(
    id = id,
    type = type,
    url = url,
    name = name
)

/**
 * Mapper to map list of [FilesRemoteResponse] to list of [FilesUiData].
 */
fun List<FilesRemoteResponse>.mapToUiModel(): List<FilesUiData> = map { it.mapToUiModel() }
