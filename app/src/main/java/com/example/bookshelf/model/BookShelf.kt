package com.example.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class BookShelf(
    val totalItems: Int,
    val items: List<Book>
)