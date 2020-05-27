package com.gibsoncodes.weatherapp.ui

import androidx.lifecycle.*
import com.gibsoncodes.weatherapp.AbsentLiveData
import com.gibsoncodes.weatherapp.ui.models.ForecastDataUi
import com.gibsoncodes.weatherapp.repo.ForecastRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ForecastViewModel(private val forecastRepo: ForecastRepo): ViewModel() {
  private val _city= MutableLiveData<String>()
     val city: LiveData<String>
    get() = _city

  private  val compositeDisposable=CompositeDisposable()
  private  val weatherResponse:MutableLiveData<Resource<ForecastDataUi>> = MutableLiveData()

    fun provideWeatherDataResponse():LiveData<Resource<ForecastDataUi>>{
        return weatherResponse
    }


    fun provideWeatherData(){
        loadWeatherData()
    }
    private fun loadWeatherData() {
        compositeDisposable.add(
            forecastRepo.getWeatherDataFromApi(city.value!!).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{
                    weatherResponse.value=
                        Resource.loading(null)
                }.subscribe({
                    Timber.d("Subscribed fetching data from api")
                    // weatherResponse.value= Resource.success(provideCachedData().value)
                    when {
                        it!=null -> weatherResponse.value=
                            Resource.success(it)
                        else -> weatherResponse.value=
                            Resource.success(AbsentLiveData.create<ForecastDataUi>().value)
                    }
                },{
                    Timber.e("Failed fetching data from the api")
                    weatherResponse.value=
                        Resource.error(null, it)
                })

        )
    }


    override fun onCleared() {
        super.onCleared()
    compositeDisposable.clear()
    }
    fun setCity(city:String){
        if (_city.value != city) {
            _city.value= city
        }
    }
}