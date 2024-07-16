package com.example.bookshelf.network

import com.example.bookshelf.model.BookShelf
import retrofit2.http.GET
import retrofit2.http.Query

interface BookShelfApiService {
    @GET("volumes?maxResults=40&fields=totalItems,items(id,volumeInfo/title,volumeInfo/authors,volumeInfo/publisher,volumeInfo/publishedDate,volumeInfo/description,volumeInfo/categories,volumeInfo/imageLinks/thumbnail,volumeInfo/imageLinks/thumbnail)")
    suspend fun getBookList(
        @Query("q") query: String
    ): BookShelf
}

