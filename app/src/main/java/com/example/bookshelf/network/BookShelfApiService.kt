package com.example.bookshelf.network

import com.example.bookshelf.model.BookShelf
import retrofit2.http.GET

interface BookShelfApiService {
    @GET("volumes?q=jazz+history&maxResults=40&fields=items(id,volumeInfo/title,volumeInfo/authors,volumeInfo/publisher,volumeInfo/publishedDate,volumeInfo/description,volumeInfo/imageLinks/thumbnail,volumeInfo/imageLinks/thumbnail)")
    suspend fun getBookList(): BookShelf
}

