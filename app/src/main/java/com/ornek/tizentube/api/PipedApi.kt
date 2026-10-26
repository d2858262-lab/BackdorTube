package com.ornek.tizentube.api

import retrofit2.http.GET
import retrofit2.http.Query

data class PipedVideo(
    val url: String?,
    val title: String?,
    val uploaderName: String?,
    val thumbnail: String?,
    val duration: Long?,
    val views: Long?,
    val uploadedDate: String?
)

interface PipedService {
    @GET("trending")
    suspend fun getTrending(@Query("region") region: String): List<PipedVideo>

    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("region") region: String
    ): List<PipedVideo>
}
