package com.ornek.tizentube.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ornek.tizentube.model.VideoItem
import com.ornek.tizentube.ui.components.TizenSearchBar
import com.ornek.tizentube.ui.components.VideoCard
import com.ornek.tizentube.ui.components.YouTubePlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val videos = remember { VideoItem.getMockVideos() }
    var selectedVideoId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TizenTube", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F0F0F))
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF0F0F0F)) {
                NavigationBarItem(
                    selected = true, onClick = {},
                    icon = { Icon(Icons.Default.Home, "Ana Sayfa") },
                    label = { Text("Ana Sayfa") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White, selectedTextColor = Color.White,
                        unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    selected = false, onClick = {},
                    icon = { Icon(Icons.Default.Subscriptions, "Abonelikler") },
                    label = { Text("Abonelikler") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    selected = false, onClick = {},
                    icon = { Icon(Icons.Default.VideoLibrary, "Kütüphane") },
                    label = { Text("Kütüphane") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF0F0F0F))
        ) {
            if (selectedVideoId != null) {
                YouTubePlayerView(videoId = selectedVideoId!!, autoPlay = true)
            }
            TizenSearchBar(onSearch = {})
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(videos) { video ->
                    VideoCard(video = video, onClick = { selectedVideoId = video.id })
                }
            }
        }
    }
}
