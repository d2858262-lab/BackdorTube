package com.ornek.tizentube.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ornek.tizentube.api.PipedService
import com.ornek.tizentube.api.PipedVideo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log

class VideoViewModel : ViewModel() {
    
    // HTTP 403'ü aşmak için User-Agent ekliyoruz
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .build()
            chain.proceed(request)
        }
        .build()

    // kavin.rocks yerine daha stabil olan official-server veya lunar sunucusunu deneyelim
    private val api = Retrofit.Builder()
        .baseUrl("https://pipedapi.lunar.icu/") 
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PipedService::class.java)

    private val _videos = MutableStateFlow<List<PipedVideo>>(emptyList())
    val videos: StateFlow<List<PipedVideo>> = _videos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init { fetchTrending() }

    fun fetchTrending() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try { 
                _videos.value = api.getTrending() 
            } catch (e: Exception) { 
                _errorMessage.value = "Hata: ${e.message ?: "Sunucu reddetti"}"
                Log.e("BackdorTube", "API Error", e)
            }
            _isLoading.value = false
        }
    }

    fun searchVideos(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try { 
                val response = api.search(query)
                _videos.value = response.items 
            } catch (e: Exception) {
                _errorMessage.value = "Arama Hatası: ${e.message}"
                Log.e("BackdorTube", "Search Error", e)
            }
            _isLoading.value = false
        }
    }
}
