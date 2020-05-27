package com.gibsoncodes.weatherapp.ui.models


data class ForecastDataUi(
    val name:String,
    val country:String,
    val temp:Float,
    val pressure:Float,
    val humidity:Float,
    val tempMin:Float,
    val tempMax:Float,
    val cod:Int,
    val coord: Coord,
    val wind: Wind,
    val weather: Weather
) {

    data class Coord(val lon:Float,val lat:Float)
    data class Wind(val speed:Float,val deg:Float)
    data class Weather(val main:String,val description:String,val icon:String)
    companion object{
        fun convertResponseToDataEntity(forecastResponse: ForecastResponse): ForecastDataUi {
            return ForecastDataUi(
                forecastResponse.name,
                forecastResponse.sys.country,
                forecastResponse.main.temp,
                forecastResponse.main.pressure,
                forecastResponse.main.humidity,
                forecastResponse.main.tempMin,
                forecastResponse.main.tempMin,
                forecastResponse.cod,
                Coord(
                    forecastResponse.coord.lon,
                    forecastResponse.coord.lat
                ),
                Wind(
                    forecastResponse.wind.speed,
                    forecastResponse.wind.deg
                ),
                Weather(
                    forecastResponse.weather[0].main,
                    forecastResponse.weather[0].description,
                    forecastResponse.weather[0].icon
                )
            )
        }
    }
}