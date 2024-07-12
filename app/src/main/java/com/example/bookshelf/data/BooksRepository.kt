package com.example.bookshelf.data

import com.example.bookshelf.model.BookShelf
import com.example.bookshelf.network.BookShelfApiService

interface BookShelfRepository {
    suspend fun getBookList(query: String): BookShelf
}

class NetworkBookShelfRepository(
    private val bookShelfApiService: BookShelfApiService
): BookShelfRepository {
    override suspend fun getBookList(query: String): BookShelf = bookShelfApiService.getBookList(query)
}