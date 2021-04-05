package com.rakesh.myweatherapp.api

import com.rakesh.myweatherapp.model.ForecastResponse
import com.rakesh.myweatherapp.model.WeatherResponse
import com.rakesh.myweatherapp.util.Constant
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIClient {

    private val api = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(WeatherAPI::class.java)

    fun getDataFromCity(
        city: String,
        units: String
    ): Single<WeatherResponse> {
        return api.callApiForWeatherInfo(city)
    }

    fun getForecastFiveDays(
        city: String,
        units: String
    ): Single<ForecastResponse> {
        return api.getForecastByCity(city, units)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(RequestInterceptor())
        return client.build()
    }

}