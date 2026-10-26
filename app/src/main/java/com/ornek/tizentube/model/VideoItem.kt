package com.ornek.tizentube.model

data class VideoItem(
    val id: String,
    val title: String,
    val channel: String,
    val thumbnailUrl: String,
    val duration: String,
    val views: String,
    val uploadTime: String
) {
    companion object {
        fun getMockVideos(): List<VideoItem> = listOf(
            VideoItem("dQw4w9WgXcQ", "Rick Astley - Never Gonna Give You Up", "Rick Astley", "https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg", "3:33", "1.4B", "15 yıl önce"),
            VideoItem("jNQXAC9IVRw", "Me at the zoo", "jawed", "https://img.youtube.com/vi/jNQXAC9IVRw/hqdefault.jpg", "0:19", "290M", "19 yıl önce"),
            VideoItem("9bZkp7q19f0", "PSY - GANGNAM STYLE", "officialpsy", "https://img.youtube.com/vi/9bZkp7q19f0/hqdefault.jpg", "4:13", "4.9B", "12 yıl önce"),
            VideoItem("kJQP7kiw5Fk", "Luis Fonsi - Despacito", "Luis Fonsi", "https://img.youtube.com/vi/kJQP7kiw5Fk/hqdefault.jpg", "4:42", "8.3B", "7 yıl önce"),
            VideoItem("JGwWNGJdvx8", "Ed Sheeran - Shape of You", "Ed Sheeran", "https://img.youtube.com/vi/JGwWNGJdvx8/hqdefault.jpg", "4:24", "6.2B", "7 yıl önce")
        )
    }
}
