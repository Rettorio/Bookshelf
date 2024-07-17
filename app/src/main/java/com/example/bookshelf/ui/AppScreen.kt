package com.example.bookshelf.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.bookshelf.navigation.LocalNavController
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen

@Serializable
data class BookDetail(
    val authors: List<String>?,
    val title: String,
    val thumbnail: String?,
    val publishedDate: String?,
    val publisher: String?,
    val description: String?,
    val categories: List<String>?
)



@Composable
fun BookAppView() {
    val navController = LocalNavController.current
    val viewModel: BookShelfViewModel = viewModel(factory = BookShelfViewModel.factory)
    NavHost(navController = navController, startDestination = HomeScreen) {
        composable<HomeScreen> {
            BookShelfScreen(
                viewModel = viewModel
            )
        }
        composable<BookDetail> {
            val args = it.toRoute<BookDetail>()
            BookInfoScreen(
                title = args.title,
                authors = args.authors,
                thumbnail = args.thumbnail,
                publishedDate = args.publishedDate,
                publisher = args.publisher,
                description = args.description,
                categories = args.categories,
                onSearchCategory = {x -> viewModel.searchBook(x) }
            )
        }
    }
}