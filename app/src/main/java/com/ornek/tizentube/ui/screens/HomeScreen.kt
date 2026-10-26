package com.ornek.tizentube.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var selectedVideoId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BackdorTube", color = Color.Red) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.Black) {
                NavigationBarItem(selected = true, onClick = { vm.fetchTrending() }, 
                    icon = { Icon(Icons.Default.Home, "Trend") }, label = { Text("Trend") })
                NavigationBarItem(selected = false, onClick = { }, 
                    icon = { Icon(Icons.Default.Subscriptions, "Takip") }, label = { Text("Takip") })
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color.Black)) {
            if (selectedVideoId != null) {
                YouTubePlayerView(videoId = selectedVideoId!!, autoPlay = true)
            }
            TizenSearchBar(onSearch = { query -> vm.searchVideos(query) })
            
            LazyColumn {
                items(pipedVideos) { v ->
                    val videoId = v.url.split("v=").last()
                    VideoCard(
                        video = VideoItem(videoId, v.title, v.uploaderName, v.thumbnail, 
                            "${v.duration/60}:${v.duration%60}", "${v.views/1000}B", v.uploadedDate),
                        onClick = { selectedVideoId = videoId }
                    )
                }
            }
        }
    }
}
