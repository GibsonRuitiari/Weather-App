package com.gibsoncodes.weatherapp.ui.models

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("main")
val main: Main,
    @SerializedName("wind")
val wind: Wind,
    @SerializedName("dt")
val dt:Double,
    @SerializedName("sys")
val sys: Sys,
    @SerializedName("id")
val id:Double,
    @SerializedName("name")
val name:String,
    @SerializedName("cod")
val cod:Int,
    @SerializedName("weather")
val weather:List<Weather>
) {

data class Coord(@SerializedName("lon")
                 val lon:Float,
                 @SerializedName("lat")
    val lat:Float)


  data  class Main(@SerializedName("temp")
                   val temp:Float,
                   @SerializedName("pressure")
    val pressure:Float,
    @SerializedName("humidity")
    val humidity:Float,
    @SerializedName("temp_min")
    val tempMin:Float,
    @SerializedName("temp_max")
    val tempMax:Float)
   data class Wind(@SerializedName("speed")
                   val speed:Float,
                   @SerializedName("deg")
    val deg:Float)
 data   class Sys(@SerializedName("country")
                  val country :String)


   data class Weather( @SerializedName("id")
                       val id:Int,
                       @SerializedName("main")
    val main:String,
    @SerializedName("description")
    val description:String,
    @SerializedName("icon")
    val icon:String)



}