package com.rakesh.myweatherapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rakesh.myweatherapp.api.WeatherAPIClient
import com.rakesh.myweatherapp.model.WeatherResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    // val currentLocationData by lazy { MutableLiveData<WeatherResponse>() }

    private val apiClient = WeatherAPIClient()
    private val disposable = CompositeDisposable()

    val locationData = MutableLiveData<WeatherResponse>()
    val locationLoading = MutableLiveData<Boolean>()

    fun getWeatherDataWithGPS(city: String, units: String) {
        locationLoading.value = true
        disposable.add(
            apiClient.getDataFromCity(city, units)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherResponse>() {
                    override fun onSuccess(t: WeatherResponse) {
                        locationData.value = t
                        locationLoading.value = false
                        Log.i("BİLGİ : ", "CALIŞTI")
                    }

                    override fun onError(e: Throwable) {
                        Log.i("BİLGİ : ", "HATA" + e.message + " " + e.printStackTrace())

                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}