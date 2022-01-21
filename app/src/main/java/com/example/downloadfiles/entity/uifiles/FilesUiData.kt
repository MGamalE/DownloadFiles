package com.example.downloadfiles.entity.uifiles

data class FilesUiData(
    val id: Int? = null,
    val type: String? = null,
    val url: String? = null,
    val name: String? = "",
    val status: DownloadStatus = DownloadStatus.PENDING,
    val position: Int = -1
)
