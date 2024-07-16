package com.example.bookshelf.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.navigation.LocalNavController
import com.example.bookshelf.ui.theme.surfaceContainer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    navigateUp: () -> Unit
) {
    TopAppBar(
        title = {
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = {  },
    )
}

@Composable
fun BookInfoScreen(
    title: String,
    publishedDate: String?,
    authors: List<String>?,
    publisher: String?,
    description: String?,
    thumbnail: String?,
    categories: List<String>?
) {
    val navController = LocalNavController.current
    val titleWordCounter = title.toList().size
    val titleStyle = when(titleWordCounter) {
        in 0..45 -> MaterialTheme.typography.headlineMedium
        in 46..60 -> MaterialTheme.typography.headlineSmall
        else -> MaterialTheme.typography.titleMedium
    }

    Log.d("BKI", "title size: $titleWordCounter")

    Scaffold(
        topBar = { TopAppBar(navigateUp = { navController.navigateUp() }) }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 24.dp, start = 18.dp, end = 18.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BookPhoto(thumbnail = thumbnail)
                Spacer(Modifier.width(12.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(title, style = titleStyle)
                    Spacer(modifier = Modifier.height(4.dp))
                    ShowAttribute(icon = Icons.Default.Person, body = authors?.joinToString() ?: "-" )
                    ShowAttribute(icon = Icons.Default.CorporateFare, body = publisher ?: "-Unknown Publisher")
                    ShowAttribute(icon = Icons.Default.Event, body = publishedDate ?: "-Unknown Date")
                }
            }
            if (categories != null) {
                Spacer(Modifier.height(16.dp))
                ShowCategories(categories)
            }
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = surfaceContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .heightIn(max = 420.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Description :", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp))
                    Text(
                        text = description ?: "-",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
fun BookPhoto(
    modifier: Modifier = Modifier,
    thumbnail: String?
) {
    Card(
        modifier = modifier
            .height(144.dp)
            .width(112.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceContainer
        )
    ) {
        Box(Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = null
            )
        }
    }
}

@Composable
private fun ShowAttribute(
    icon: ImageVector,
    body: String
) {
    Row {
       Box(modifier = Modifier.size(16.dp)) {
           Icon(
               imageVector = icon,
               contentDescription = null,
               modifier = Modifier.fillMaxSize()
           )
       }
        Spacer(Modifier.width(4.dp))
        Text(body, style = MaterialTheme.typography.labelMedium)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShowCategories(
    categories: List<String>
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        categories.forEach { category ->
            SuggestionChip(
                onClick = { /*TODO*/ },
                label = { Text(category) }
            )
        }
    }
}