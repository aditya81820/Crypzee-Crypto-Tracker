package com.adityat.crypzee_cryptotrackingapp.MainUI

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.adityat.crypzee_cryptotrackingapp.Article
import com.adityat.crypzee_cryptotrackingapp.R
import com.adityat.crypzee_cryptotrackingapp.Viewmodel.MainViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun Explore(viewModel: MainViewModel) {
    // Observe the news articles
    val articles by viewModel.newsArticles
    val isLoading by viewModel.isLoadingnews
    LaunchedEffect("1a5181a3ee0248a883ed40aef109cfb7") {
        viewModel.fetchNews("1a5181a3ee0248a883ed40aef109cfb7")

    }
    Column() {
        // Show loading indicator if data is loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White
                )
            }
        } else {
            // UI to display the news articles
            LazyColumn {
                items(articles) { article ->
                    NewsArticle(article)
                }
            }
        }
    }


}

@Composable
fun NewsArticle(article: Article) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = article.urlToImage,
        placeholder = painterResource(R.drawable.placeholder),
        error = painterResource(R.drawable.error)
    )
    val url = article.url


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .height(80.dp)
                .width(120.dp),
            contentScale = ContentScale.Crop,


            )
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = article.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.secondary
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val formattedDate = try {
                    // Parsing and formatting in one step
                    Instant.parse(article.publishedAt)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) // Customize format if needed
                } catch (e: Exception) {
                    "Invalid date"
                }
                Text(
                    text = "Last Updated : ${formattedDate}",
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Source : ${article.source.name}",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

        }


    }

}