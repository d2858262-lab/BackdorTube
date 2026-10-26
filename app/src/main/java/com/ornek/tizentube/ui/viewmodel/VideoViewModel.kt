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
import com.google.gson.GsonBuilder
import android.util.Log

class VideoViewModel : ViewModel() {
    
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36")
                .header("Accept", "application/json") // Sadece JSON istediğimizi belirttik
                .build()
            chain.proceed(request)
        }
        .build()

    // Hata mesajında istenen 'setLenient' özelliğini ekliyoruz
    private val gson = GsonBuilder()
        .setLenient() 
        .create()

    // Çok daha stabil olan Garuda Linux Piped Instance'ını kullanıyoruz
    private val api = Retrofit.Builder()
        .baseUrl("https://piped-api.garudalinux.org/") 
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
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
                _errorMessage.value = "Sunucu Hatası: ${e.message ?: "Veri alınamadı"}"
                Log.e("BackdorTube", "Trending Error", e)
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
                _errorMessage.value = "Arama Hatası: Sunucu meşgul, lütfen tekrar deneyin."
                Log.e("BackdorTube", "Search Error", e)
            }
            _isLoading.value = false
        }
    }
}
