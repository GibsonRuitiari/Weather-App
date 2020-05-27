package com.gibsoncodes.weatherapp.repo


import com.gibsoncodes.weatherapp.network.ApiService
import com.gibsoncodes.weatherapp.ui.models.ForecastDataUi
import io.reactivex.Observable

import timber.log.Timber

class ForecastRepo constructor(private val apiService: ApiService) {

   fun getWeatherDataFromApi(city:String): Observable<ForecastDataUi> {
      return apiService.getWeatherData(city).toObservable()?.doOnNext{
          Timber.d("Getting data from api ${it.name}")
      }?.map{
          ForecastDataUi.convertResponseToDataEntity(it)
      }?.doOnError { Timber.e("Failed to fetch data from api") }!!
  }



}