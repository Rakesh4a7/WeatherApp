package com.rakesh.myweatherapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rakesh.myweatherapp.adapter.CustomAdapter
import com.rakesh.myweatherapp.model.WeatherResponse
import com.winterbitegames.myweatherapp.common.ClickListener
import com.winterbitegames.myweatherapp.viewmodel.WeatherInfoViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), ClickListener {
    private lateinit var viewModel: WeatherInfoViewModel
    private var cityList = ArrayList<WeatherResponse>()
    private lateinit var customAdapter: CustomAdapter
    private val sharedPrefFile = "mypreference"
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        sharedPreferences = this.getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
        viewModel = ViewModelProviders.of(this).get(WeatherInfoViewModel::class.java)

        setLiveDataListeners()
        getList()

        customAdapter = CustomAdapter(cityList, this)
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = customAdapter
    }

    override fun onClick(data: Any, position: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("city",(data as WeatherResponse).name)
        startActivity(intent)
    }

    private fun setLiveDataListeners() {
        viewModel.progressBarLiveData.observe(this, Observer { isShowLoader ->
            if (isShowLoader)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.GONE
        })

        viewModel.weatherInfoLiveData.observe(this, { weatherData ->
            setWeatherInfo(weatherData)
        })

        viewModel.weatherInfoList.observe(this, { weatherList ->
            cityList = weatherList
            customAdapter.updateList(weatherList)
        })

        viewModel.weatherInfoFailureLiveData.observe(this, {
            Toast.makeText(this, "Could not get weather. Please add valid city name and try again.", Toast.LENGTH_SHORT)
                .show()
        })
    }

    private fun setWeatherInfo(weatherData: WeatherResponse) {
        // Pass this data to adapter
        cityList.add(weatherData);
        customAdapter.updateList(cityList)
    }

    private fun getList() {
        if (sharedPreferences != null) {
            sharedPreferences = this.getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
            val listString = sharedPreferences!!.getString("cityList", "")
            if (!listString.equals("") || listString?.length != 0) {
                val cityNames: List<String> = listString?.split(",")!!.map { it.trim() }
                viewModel.getWeatherInfo(cityNames)
            } else {
                val cityNames: List<String> = listOf("Delhi","Hyderabad","Mumbai","Chennai")
                viewModel.getWeatherInfo(cityNames)
            }
        }
    }

    private fun updateListToDb(list: ArrayList<WeatherResponse>) {
        if (sharedPreferences != null) {
            sharedPreferences = this.getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            var nameList = mutableListOf<String>()
            for (i in list.indices){
                nameList.add(list[i].name!!)
            }
            editor.putString("cityList", nameList.joinToString())
            editor.apply()
        }
    }

    override fun onStop() {
        super.onStop()
        updateListToDb(cityList)
    }

}