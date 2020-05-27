package com.gibsoncodes.weatherapp.appcomponent

import android.app.Application
import com.gibsoncodes.weatherapp.R
import com.gibsoncodes.weatherapp.appconfig.Config
import com.gibsoncodes.weatherapp.network.ApiService
import com.gibsoncodes.weatherapp.repo.ForecastRepo
import com.gibsoncodes.weatherapp.ui.ForecastViewModel

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class App: Application() {
    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var okHttpClient: OkHttpClient
        private lateinit var apiService: ApiService
        private lateinit var forecastRepo: ForecastRepo
        private lateinit var googleSignInOptions: GoogleSignInOptions
        private lateinit var forecastViewModel: ForecastViewModel
        fun injectGoogleSignInOptions()=
            googleSignInOptions
        fun injectAppApi() = apiService
    }
    override fun onCreate() {
        super.onCreate()
    Timber.uprootAll()
        Timber.plant(Timber.DebugTree())
        initClient()
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(Config.baseUrl)
            .build()

        apiService = retrofit.create(ApiService::class.java)

        forecastRepo = ForecastRepo(apiService)
        googleSignInOptions =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        forecastViewModel = ForecastViewModel(
            forecastRepo
        )
    }
    private fun initClient(){
       val clientBuilder:OkHttpClient.Builder= OkHttpClient.Builder()
           .readTimeout(60,TimeUnit.SECONDS)
           .writeTimeout(60,TimeUnit.SECONDS)
           .connectTimeout(60,TimeUnit.SECONDS)
        val interceptor:HttpLoggingInterceptor= HttpLoggingInterceptor().apply {
            level= HttpLoggingInterceptor.Level.BODY
        }
        clientBuilder.addInterceptor(interceptor)
        clientBuilder.addInterceptor{
            val originalRequest= it.request()
            val originalUrl:HttpUrl= originalRequest.url()
            val newUrl= originalUrl.newBuilder().setQueryParameter("appid",
                Config.apiKey).build()
            val requestBuilder= originalRequest.newBuilder()
                .url(newUrl)
                .addHeader("Accept","application/json")
                .addHeader("Content-Type","application/json")
            return@addInterceptor it.proceed(requestBuilder.build())
        }
       okHttpClient =clientBuilder.build()
    }
}