package com.example.bookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.ui.BookAppView
import com.example.bookshelf.ui.BookShelfViewModel
import com.example.bookshelf.ui.theme.BookshelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookshelfTheme {
                val viewModel: BookShelfViewModel = viewModel(factory = BookShelfViewModel.factory)
                // A surface container using the 'background' color from the theme
                BookAppView(viewModel)
            }
        }
    }
}
