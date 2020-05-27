package com.gibsoncodes.weatherapp.network

import com.gibsoncodes.weatherapp.ui.models.ForecastResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
     fun getWeatherData(@Query("q") cityQuery:String):
            Single<ForecastResponse>
}