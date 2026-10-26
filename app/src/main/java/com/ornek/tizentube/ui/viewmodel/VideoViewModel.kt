package com.ornek.tizentube.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ornek.tizentube.api.PipedService
import com.ornek.tizentube.api.PipedVideo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoViewModel : ViewModel() {
    private val api = Retrofit.Builder()
        .baseUrl("https://pipedapi.kavin.rocks/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PipedService::class.java)

    private val _videos = MutableStateFlow<List<PipedVideo>>(emptyList())
    val videos: StateFlow<List<PipedVideo>> = _videos

    init { fetchTrending() }

    fun fetchTrending() {
        viewModelScope.launch {
            try { _videos.value = api.getTrending() } catch (e: Exception) { }
        }
    }

    fun searchVideos(query: String) {
        viewModelScope.launch {
            try { _videos.value = api.search(query).items } catch (e: Exception) { }
        }
    }
}
