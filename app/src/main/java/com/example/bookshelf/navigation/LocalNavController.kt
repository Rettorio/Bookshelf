package com.example.bookshelf.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController> { error("No Controller Found!") }