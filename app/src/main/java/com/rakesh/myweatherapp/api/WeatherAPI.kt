package com.rakesh.myweatherapp.api

import com.rakesh.myweatherapp.model.ForecastResponse
import com.rakesh.myweatherapp.model.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("forecast?appid=da7b24afc414d0fa1b4f5afc4d7befa0&")
    fun getForecastByCity(@Query("q") city: String, @Query("units") units: String): Single<ForecastResponse>

    @GET("weather?appid=da7b24afc414d0fa1b4f5afc4d7befa0&")
    fun callApiForWeatherInfo(@Query("q") city: String): Single<WeatherResponse>
}