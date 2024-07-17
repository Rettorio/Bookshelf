package com.example.bookshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Plagiarism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.VolumeInfo
import com.example.bookshelf.navigation.LocalNavController
import com.example.bookshelf.ui.theme.surfaceContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSort: () -> Unit,
    onSearch: () -> Unit
) {
    TopAppBar(
        title = {
            Row {
                Text("BookShelf", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Outlined.LocalLibrary, null)
            }
        },
        actions = {
            IconButton(onClick =  onSearch ) {
                Icon(Icons.Default.TravelExplore, null)
            }
            IconButton(onClick = onSort ) {
                Icon(Icons.AutoMirrored.Filled.Sort, null)
            }
        },
        scrollBehavior = scrollBehavior
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookShelfScreen(
    viewModel: BookShelfViewModel
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val appUiState: BookShelfUiState = viewModel.appUiState
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            AppTopBar(
                scrollBehavior = scrollBehavior,
                onSort = { viewModel.sortBookList() },
                onSearch = { viewModel.openSearchDialog = true }
            )
        }
    ) {innerPadding ->
        when (appUiState) {
            BookShelfUiState.Error -> ErrorScreen(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .fillMaxHeight(.5f),
                retryAction = { viewModel.getBookList() }
            )

            BookShelfUiState.Loading -> LoadingScreen(
                Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
            )
            is BookShelfUiState.Success -> {
                BookShelfList(
                    modifier = Modifier.padding(innerPadding),
                    querySearch = viewModel.querySearch.replace("+", " "),
                    bookList = appUiState.bookList,
                    totalItem = appUiState.totalItem,
                    viewBookInfo = { navController.navigate(it) }
                )
                if(viewModel.openSearchDialog) {
                    QueryInputDialog(
                        onSave = { viewModel.searchBook(it) },
                        onCancel = { viewModel.openSearchDialog = false },
                        defaultQuery = viewModel.querySearch
                    )
                }
            }
        }

    }
}

@Composable
private fun BookShelfList(
    modifier: Modifier = Modifier,
    querySearch: String,
    bookList: List<Book>,
    totalItem: Int,
    viewBookInfo: (BookDetail) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.padding(top = 8.dp, start = 2.dp, end = 2.dp),
        columns = GridCells.Adaptive(minSize = 114.dp)
    ) {
        header {
            QueryTextResult(
                modifier = Modifier
                    .padding(bottom = 12.dp, top = 4.dp)
                    .width(300.dp),
                querySearch = querySearch,
                totalItem = totalItem
            )
        }
        items(bookList) {book ->
            BookItem(
                bookInfo = book.volumeInfo,
                modifier = Modifier.padding(6.dp),
                onClick = viewBookInfo
            )

        }
    }
}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

@Composable
private fun LoadingScreen(modifier: Modifier) {
    Column(
        modifier = modifier.padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(32.dp))
        Text("Loading...")
    }
}

@Composable
private fun ErrorScreen(
    modifier: Modifier,
    retryAction: () -> Unit
) {
    Column(
        modifier = modifier.padding(top = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(48.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_connection_error),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.padding(top = 8.dp))
        Text("No Internet Connection.")
        TextButton(onClick = retryAction) {
            Text("TRY AGAIN", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookItem(
    modifier: Modifier = Modifier,
    bookInfo: VolumeInfo,
    onClick: (BookDetail) -> Unit
) {
    val imgUrl = bookInfo.imageLinks?.thumbnail?.replace("http", "https")

    Card(
        modifier = modifier,
        onClick = { onClick(
            BookDetail(
                title = bookInfo.title,
                authors = bookInfo.authors,
                publisher = bookInfo.publisher,
                publishedDate = bookInfo.publishedDate,
                description = bookInfo.description,
                thumbnail = bookInfo.imageLinks?.thumbnail?.replace("http", "https"),
                categories = bookInfo.categories
            )
        ) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceContainer
        )
    ) {
        Box(modifier = Modifier
            .height(164.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imgUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = null,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 300f
                    )
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(bookInfo.title, style = MaterialTheme.typography.labelMedium, color = Color.White)
            }
        }
    }
}


@Composable
private fun QueryInputDialog(
    onSave: (String) -> Unit,
    onCancel: () -> Unit,
    defaultQuery: String
) {
    var searchQuery: String by remember { mutableStateOf(defaultQuery.replace("+", " ")) }
    AlertDialog(
        onDismissRequest = onCancel,
        icon = {
          Box(modifier = Modifier.size(24.dp)) {
              Icon(Icons.Outlined.Plagiarism, null, modifier = Modifier.fillMaxSize())
          }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(searchQuery.replace(" ", "+")) },
                enabled = searchQuery.isNotEmpty()
            ) {
                Text("Search")
            }
        },
        dismissButton = {
            TextButton(onClick =  onCancel ) {
                Text("Cancel")
            }
        },
        title = {
            Text(
                "Input Anything.",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("title,genres,author,etc")
                },
                keyboardActions = KeyboardActions(onSearch = { onSave(searchQuery) } ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )
        }
    )
}

@Composable
private fun QueryTextResult(
    modifier: Modifier = Modifier,
    querySearch: String = "Jazz History",
    totalItem: Int = 389
) {
    val sourceText = "Total Item $totalItem shows 40"
    val detailText = textHighlightGenerator(
        sourceText,
        listOf(totalItem.toString(), "40")
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "\"$querySearch\"",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(detailText, color = MaterialTheme.colorScheme.onSurface)
    }
}

fun textHighlightGenerator(
    source: String,
    segments: List<String>
) : AnnotatedString {
    val builder = AnnotatedString.Builder()
    val highlightStyle = SpanStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
    )
    val normalStyle = SpanStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
    builder.append(source)
    builder.addStyle(normalStyle, 0, source.length)

    segments.forEach { segment ->
        val start = source.indexOf(segment)
        val end = start + segment.length
        builder.addStyle(highlightStyle, start, end)
    }

    return builder.toAnnotatedString()
}