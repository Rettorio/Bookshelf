package com.example.bookshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookShelfApplication
import com.example.bookshelf.data.BookShelfRepository
import com.example.bookshelf.model.Book
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookShelfUiState {
    data class Success(
        val bookList: List<Book>
    ): BookShelfUiState
    data object Loading: BookShelfUiState
    data object Error : BookShelfUiState
}

class BookShelfViewModel(
    private val bookShelfRepository: BookShelfRepository
): ViewModel() {
    var appUiState: BookShelfUiState by mutableStateOf(BookShelfUiState.Loading)
        private set

    private var bookAscendSorted: Boolean by mutableStateOf(false)
    private var querySearch: String by mutableStateOf("jazz+history")
    var openSearchDialog: Boolean by mutableStateOf(false)


    init {
        getBookList()
    }


    private fun getBookList() {
        viewModelScope.launch {
            appUiState = BookShelfUiState.Loading
            appUiState = try {
                BookShelfUiState.Success(bookShelfRepository.getBookList(querySearch).items)
            } catch (e: HttpException) {
                BookShelfUiState.Error
            } catch (e: IOException) {
                BookShelfUiState.Error
            }
        }
    }

    fun sortBookList() {
        val books: List<Book>? = when(appUiState) {
            BookShelfUiState.Error -> null
            BookShelfUiState.Loading -> null
            is BookShelfUiState.Success -> (appUiState as BookShelfUiState.Success).bookList
        }
        if (books != null) {
            if(!bookAscendSorted) {
                val ascendBooks: List<Book> = books.sortedBy { it.volumeInfo.title }
                appUiState = BookShelfUiState.Success(ascendBooks)
                bookAscendSorted = true
            } else {
                val descendBooks: List<Book> = books.sortedByDescending { it.volumeInfo.title }
                appUiState = BookShelfUiState.Success(descendBooks)
                bookAscendSorted = false
            }
        }
    }

    fun searchBook(query: String) {
        querySearch = query.replace(" ", "+")
        openSearchDialog = false
        getBookList()
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY]) as BookShelfApplication
                val bookShelfRepository = app.container.bookShelfRepository
                BookShelfViewModel(bookShelfRepository)
            }
        }
    }
}