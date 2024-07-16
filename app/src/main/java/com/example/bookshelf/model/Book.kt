package com.example.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
)

@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val categories: List<String>?
)

@Serializable
data class ImageLinks(
    val thumbnail: String
)