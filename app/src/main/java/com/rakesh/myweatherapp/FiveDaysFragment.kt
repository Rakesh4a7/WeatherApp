package com.rakesh.myweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rakesh.myweatherapp.adapter.HourlyAdapter
import com.rakesh.myweatherapp.util.Constant
import com.rakesh.myweatherapp.viewmodel.FiveDaysViewModel
import kotlinx.android.synthetic.main.fragment_five_days.*


class FiveDaysFragment : Fragment() {

    private lateinit var viewModel: FiveDaysViewModel
    private val hourlyAdapter = HourlyAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_five_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProviders.of(this).get(FiveDaysViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = hourlyAdapter

        viewModel.getForecastFiveDays((activity as MainActivity).city, Constant.METRIC)

        viewModel.forecastData.observe(viewLifecycleOwner, Observer { forecastGps ->
            forecastGps?.let {
                crdFiveDays.visibility = View.VISIBLE
                hourlyAdapter.updateHourlyList(forecastGps)

            }
        })

        viewModel.fiveDaysLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if(it){
                   fiveDaysLoading.visibility = View.VISIBLE
                    crdFiveDays.visibility = View.GONE
                }else{
                    fiveDaysLoading.visibility = View.GONE
                }
            }

        })

    }

}
