package com.ornek.tizentube.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayerView(
    videoId: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = false
) {
    AndroidView(
        factory = { context ->
            YouTubePlayerView(context).apply {
                enableAutomaticInitialization = false
                initialize(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        if (autoPlay) youTubePlayer.loadVideo(videoId, 0f)
                        else youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
        },
        update = { view ->
            view.getPlayerOrThrow().loadVideo(videoId, 0f)
        },
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    )
}
