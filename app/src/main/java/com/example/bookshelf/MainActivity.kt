package com.example.bookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bookshelf.navigation.LocalNavController
import com.example.bookshelf.ui.BookAppView
import com.example.bookshelf.ui.BookShelfViewModel
import com.example.bookshelf.ui.theme.BookshelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookshelfTheme {
                val navHost = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navHost) {
                    BookAppView()
                }
                // A surface container using the 'background' color from the theme
            }
        }
    }
}
