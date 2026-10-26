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
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .header("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()

    private val gson = GsonBuilder().setLenient().create()

    // Test ettiğimiz ve 200 OK aldığımız sunucu
    private val api = Retrofit.Builder()
        .baseUrl("https://pipedapi.ducks.party/") 
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
                // Curl testindeki gibi ?region=TR ekleniyor
                val result = api.getTrending("TR")
                _videos.value = result.filter { !it.title.isNullOrEmpty() }
            } catch (e: Exception) { 
                _errorMessage.value = "Hata: Videolar yüklenemedi. (ducks.party)"
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
                val results = api.search(query, "TR")
                _videos.value = results.filter { !it.title.isNullOrEmpty() }
            } catch (e: Exception) {
                _errorMessage.value = "Arama başarısız."
                Log.e("BackdorTube", "Search Error", e)
            }
            _isLoading.value = false
        }
    }
}
