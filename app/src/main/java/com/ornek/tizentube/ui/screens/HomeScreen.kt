package com.ornek.tizentube.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ornek.tizentube.ui.components.TizenSearchBar
import com.ornek.tizentube.ui.components.VideoCard
import com.ornek.tizentube.ui.components.YouTubePlayerView
import com.ornek.tizentube.ui.viewmodel.VideoViewModel
import com.ornek.tizentube.model.VideoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: VideoViewModel = viewModel()) {
    val pipedVideos by vm.videos.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val errorMsg by vm.errorMessage.collectAsState()
    var selectedVideoId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BackdorTube", color = Color.Red) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color.Black)) {
            if (selectedVideoId != null) {
                YouTubePlayerView(videoId = selectedVideoId!!, autoPlay = true)
            }
            TizenSearchBar(onSearch = { query -> vm.searchVideos(query) })

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            } else if (errorMsg != null) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Error, null, tint = Color.Red, modifier = Modifier.size(48.dp))
                    Text(text = errorMsg!!, color = Color.White, textAlign = TextAlign.Center)
                    Button(onClick = { vm.fetchTrending() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                        Text("Tekrar Dene")
                    }
                }
            } else {
                LazyColumn {
                    items(pipedVideos) { v ->
                        // Boş değer kontrolü ve güvenli atama
                        val videoId = v.url?.split("v=")?.last() ?: ""
                        val title = v.title ?: "Başlıksız Video"
                        val uploader = v.uploaderName ?: "Bilinmeyen Kanal"
                        val thumb = v.thumbnail ?: ""
                        val dur = v.duration ?: 0L
                        val views = v.views ?: 0L
                        val date = v.uploadedDate ?: "Tarih Bilgisi Yok"

                        VideoCard(
                            video = VideoItem(
                                id = videoId,
                                title = title,
                                channel = uploader,
                                thumbnailUrl = thumb,
                                duration = "${dur / 60}:${String.format("%02d", dur % 60)}",
                                views = "${views / 1000}B",
                                uploadTime = date
                            ),
                            onClick = { selectedVideoId = videoId }
                        )
                    }
                }
            }
        }
    }
}
