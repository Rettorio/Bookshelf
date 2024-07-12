package com.example.bookshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.ImageLinks
import com.example.bookshelf.model.VolumeInfo

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
fun BookAppView(
    viewModel: BookShelfViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val appUiState = viewModel.appUiState
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppTopBar(
                scrollBehavior,
                { viewModel.sortBookList() },
                { viewModel.openSearchDialog = true }
            )
        }
    ) {innerPadding ->
        when(appUiState) {
            BookShelfUiState.Error -> ErrorScreen(modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .fillMaxHeight(.5f))
            BookShelfUiState.Loading -> LoadingScreen(modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .fillMaxHeight(.5f))
            is BookShelfUiState.Success -> {
                HomeScreen(
                    bookList = appUiState.bookList,
                    modifier = Modifier.padding(innerPadding)
                )
                if(viewModel.openSearchDialog) {
                    QueryInputDialog(
                        onSave = { x: String -> viewModel.searchBook(x) },
                        onCancel = { viewModel.openSearchDialog = false }
                    )
                }
            }
        }
    }

}

@Composable
private fun LoadingScreen(modifier: Modifier) {
    Box(modifier = modifier.padding(top = 32.dp), contentAlignment = Alignment.Center) {
        Text("Loading...")
    }
}

@Composable
private fun ErrorScreen(modifier: Modifier) {
    Box(modifier = modifier.padding(top = 32.dp), contentAlignment = Alignment.Center) {
        Text("Something went wrong.")
    }
}

@Composable
private fun QueryInputDialog(
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    var searchQuery: String by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(
                onClick = { onSave(searchQuery) },
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
                "Search Anything",
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
fun HomeScreen(
    modifier: Modifier = Modifier,
    bookList: List<Book>,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 124.dp)
    ) {
        items(bookList) {book ->
            BookItem(
                bookInfo = book.volumeInfo,
                modifier = Modifier.padding(4.dp),
                onClick = {}
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookItem(
    modifier: Modifier = Modifier,
    bookInfo: VolumeInfo,
    onClick: () -> Unit
) {
    val imgUrl = bookInfo.imageLinks?.thumbnail?.replace("http", "https") ?: "https://placehold.co/200x160?text=${bookInfo.title}"

   Card(
       modifier = modifier,
       onClick = onClick,
       elevation = CardDefaults.cardElevation(5.dp),
       shape = RoundedCornerShape(8.dp)
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

@Preview(showBackground = true, widthDp = 300, heightDp = 500)
@Composable
private fun BookItemPreview() {
    val bookItem = VolumeInfo(
        title = "The History of Jazz",
        authors = listOf("Ted Giola"),
        publisher = "Oxford University Press, USA",
        publishedDate = "1997-11-20",
        description = "Jazz is the most colorful and varied art form in the world and it was born in one of the most colorful and varied cities, New Orleans. From the seed first planted by slave dances held in Congo Square and nurtured by early ensembles led by Buddy Belden and Joe \\\"King\\\" Oliver, jazz began its long winding odyssey across America and around the world, giving flower to a thousand different forms--swing, bebop, cool jazz, jazz-rock fusion--and a thousand great musicians. Now, in The History of Jazz, Ted Gioia tells the story of this music as it has never been told before, in a book that brilliantly portrays the legendary jazz players, the breakthrough styles, and the world in which it evolved. Here are the giants of jazz and the great moments of jazz history--Jelly Roll Morton (\\\"the world's greatest hot tune writer\\\"), Louis Armstrong (whose O-keh recordings of the mid-1920s still stand as the most significant body of work that jazz has produced), Duke Ellington at the Cotton Club, cool jazz greats such as Gerry Mulligan, Stan Getz, and Lester Young, Charlie Parker's surgical precision of attack, Miles Davis's 1955 performance at the Newport Jazz Festival, Ornette Coleman's experiments with atonality, Pat Metheny's visionary extension of jazz-rock fusion, the contemporary sounds of Wynton Marsalis, and the post-modernists of the Knitting Factory. Gioia provides the reader with lively portraits of these and many other great musicians, intertwined with vibrant commentary on the music they created. Gioia also evokes the many worlds of jazz, taking the reader to the swamp lands of the Mississippi Delta, the bawdy houses of New Orleans, the rent parties of Harlem, the speakeasies of Chicago during the Jazz Age, the after hours spots of corrupt Kansas city, the Cotton Club, the Savoy, and the other locales where the history of jazz was made. And as he traces the spread of this protean form, Gioia provides much insight into the social context in which the music was born. He shows for instance how the development of technology helped promote the growth of jazz--how ragtime blossomed hand-in-hand with the spread of parlor and player pianos, and how jazz rode the growing popularity of the record industry in the 1920s. We also discover how bebop grew out of the racial unrest of the 1940s and '50s, when black players, no longer content with being \\\"entertainers,\\\" wanted to be recognized as practitioners of a serious musical form. Jazz is a chameleon art, delighting us with the ease and rapidity with which it changes colors. Now, in Ted Gioia's The History of Jazz, we have at last a book that captures all these colors on one glorious palate. Knowledgeable, vibrant, and comprehensive, it is among the small group of books that can truly be called classics of jazz literature.",
        imageLinks = ImageLinks(thumbnail = "https://books.google.com/books/content?id=C1MI_4nZyD4C&printsec=frontcover&img=1&zoom=1&edge=curl&imgtk=AFLRE70l8NsOiCS9UsvAO_0FZGJJtzRtNxl_4p-G3zrTMoTs8kKdAHiMXocWhj9cmW5MfGJtB63evIBDn4oLD6An91pMZ55caCELHOiLxic_w8bvAAt0xYiCIC2WKf2jpUa3CysBGkhO&source=gbs_api"
        )
    )
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        BookItem(bookInfo = bookItem, onClick = {})
    }
}