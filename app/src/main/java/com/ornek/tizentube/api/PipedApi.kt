package com.ornek.tizentube.api

import retrofit2.http.GET
import retrofit2.http.Query

data class PipedVideo(
    val url: String,
    val title: String,
    val uploaderName: String,
    val thumbnail: String,
    val duration: Int,
    val views: Long,
    val uploadedDate: String
)

data class SearchResponse(val items: List<PipedVideo>)

interface PipedService {
    @GET("trending?region=TR")
    suspend fun getTrending(): List<PipedVideo>

    @GET("search")
    suspend fun search(@Query("q") query: String): SearchResponse
}
