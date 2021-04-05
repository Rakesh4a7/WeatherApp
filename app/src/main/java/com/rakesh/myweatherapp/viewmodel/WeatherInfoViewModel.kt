package com.winterbitegames.myweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rakesh.myweatherapp.api.WeatherAPIClient
import com.rakesh.myweatherapp.model.WeatherResponse
import com.rakesh.myweatherapp.util.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class WeatherInfoViewModel : ViewModel() {
    private val apiClient = WeatherAPIClient()
    private val disposable = CompositeDisposable()

    val weatherInfoLiveData = MutableLiveData<WeatherResponse>()
    val weatherInfoList = MutableLiveData<ArrayList<WeatherResponse>>()
    val weatherInfoFailureLiveData = MutableLiveData<String>()
    val progressBarLiveData = MutableLiveData<Boolean>()

    fun getWeatherInfo(cityList: List<String>) {

        progressBarLiveData.postValue(true) // PUSH data to LiveData object to show progress bar

        var cityData: ArrayList<WeatherResponse> = ArrayList()

        for (city in cityList) {

            disposable.add(
                apiClient.getDataFromCity(city, Constant.METRIC)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<WeatherResponse>() {
                        override fun onSuccess(t: WeatherResponse) {
                            cityData.add(t)

                            if (cityData.size == cityList.size) {
                                progressBarLiveData.postValue(false) // PUSH data to LiveData object to hide progress bar
                                weatherInfoList.postValue(cityData) // PUSH data to LiveData object
                            }
                        }

                        override fun onError(e: Throwable) {
                            cityData.add(0, WeatherResponse())
                            progressBarLiveData.postValue(false) // hide progress bar
                        }
                    })
            )
        }
    }

}