package com.rakesh.myweatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rakesh.myweatherapp.R
import com.rakesh.myweatherapp.databinding.CitiesItemBinding
import com.rakesh.myweatherapp.model.WeatherResponse
import com.winterbitegames.myweatherapp.common.ClickListener

class CustomAdapter(private var cityList: ArrayList<WeatherResponse>, var clickListener: ClickListener) :
    RecyclerView.Adapter<CustomAdapter.ContactHolder>() {

    class ContactHolder(var view: CitiesItemBinding) :
        RecyclerView.ViewHolder(view.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<CitiesItemBinding>(
            inflater,
            R.layout.cities_item,
            parent,
            false
        )
        return ContactHolder(view)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.view.cityData = cityList[position]

        holder.view.tvCityTemp.text = cityList[position].main?.temp?.toInt().toString()

        holder.itemView.setOnClickListener {
            clickListener.onClick(cityList[position],position)
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun updateList(cityList: ArrayList<WeatherResponse>){
        this.cityList.clear();
        this.cityList = cityList
        notifyDataSetChanged()
    }

}