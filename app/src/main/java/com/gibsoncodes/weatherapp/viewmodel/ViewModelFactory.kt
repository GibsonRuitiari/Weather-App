package com.gibsoncodes.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gibsoncodes.weatherapp.repo.ForecastRepo

class ViewModelFactory(private val forecastRepo: ForecastRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return modelClass.getConstructor(ForecastRepo::class.java).newInstance(forecastRepo)
    }

}