package com.example.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class BookShelf(
    val items: List<Book>
)