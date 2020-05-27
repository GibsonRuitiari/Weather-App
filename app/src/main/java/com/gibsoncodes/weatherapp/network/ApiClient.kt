package com.gibsoncodes.weatherapp.network

import com.gibsoncodes.weatherapp.appconfig.Config
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object{
        private var retrofit:Retrofit?=null
        private const val timeOutRequest= 60
         private var client: OkHttpClient?=null
        fun getClient():Retrofit{
            if(client ==null){
                initClient()
            }
            if(retrofit ==null){
                retrofit =Retrofit.Builder()
                    .baseUrl(Config.baseUrl)
                    .client(client!!)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
        private fun initClient(){
            val clientBuilder:OkHttpClient.Builder= OkHttpClient().newBuilder()
                .connectTimeout(timeOutRequest.toLong(),TimeUnit.SECONDS)
                .readTimeout(timeOutRequest.toLong(),TimeUnit.SECONDS)
                .writeTimeout(timeOutRequest.toLong(),TimeUnit.SECONDS)
            val httpInterceptor= HttpLoggingInterceptor()
            httpInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            clientBuilder.addInterceptor(httpInterceptor)
            clientBuilder.addInterceptor {
                val original:Request= it.request()
                val originalUrl:HttpUrl = original.url()
                val url=originalUrl.newBuilder().addQueryParameter("appid",
                    Config.apiKey
                ).build()

                val requestBuilder:Request.Builder=original.newBuilder()
                    .url(url)
                    .addHeader("Accept","application/json")
                    .addHeader("Content-Type","application/json")
            return@addInterceptor it.proceed(requestBuilder.build())
            }
            client = clientBuilder.build()
        }
    }

}