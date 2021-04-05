package com.rakesh.myweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rakesh.myweatherapp.databinding.FragmentLocationBinding
import com.rakesh.myweatherapp.util.Constant
import com.rakesh.myweatherapp.util.dateConverter
import com.rakesh.myweatherapp.util.timeConverter
import com.rakesh.myweatherapp.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.fragment_location.*

class LocationFragment : Fragment() {

    private lateinit var viewModel: LocationViewModel
    private lateinit var dataBinding: FragmentLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        viewModel.getWeatherDataWithGPS((activity as MainActivity).city, Constant.METRIC)

        viewModel.locationData.observe(viewLifecycleOwner, Observer { locationGps ->
            locationGps?.let {
                lytLocation.visibility = View.VISIBLE
                dataBinding.locationGPS = locationGps
                dataBinding.tvTemperature.text = locationGps.main!!.temp.toInt().toString()
                dataBinding.tvDate.text = dateConverter()
                dataBinding.tvSunrise.text = timeConverter((locationGps.sys!!.sunrise).toLong())
                dataBinding.tvSunset.text = timeConverter((locationGps.sys!!.sunset).toLong())
                dataBinding.imgState.setImageResource(resources.getIdentifier("ic_"+locationGps.weather?.get(0)?.icon, "drawable", view.context.packageName))

            }
        })

        viewModel.locationLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it){
                    locationLoading.visibility = View.VISIBLE
                    lytLocation.visibility = View.GONE
                }else{
                    locationLoading.visibility = View.GONE
                }
            }
        })

    }

}
