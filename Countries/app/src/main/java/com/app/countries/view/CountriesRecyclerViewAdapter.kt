package com.app.countries.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.countries.viewmodel.CountriesViewModel
import kotlinx.android.synthetic.main.country_item_layout.view.*

class CountriesRecyclerViewAdapter(
    private var countryList: List<CountriesViewModel.Country>,
    private val onCountryClick: (regionalAcronym: ArrayList<String>) -> Unit = {}
) : RecyclerView.Adapter<CountriesRecyclerViewAdapter.CountryViewHolder>() {

    class CountryViewHolder(val countryView : View) : RecyclerView.ViewHolder(countryView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val countryItemView =  LayoutInflater.from(parent.context).inflate(R.layout.country_item_layout, parent, false)
        return CountryViewHolder(countryItemView)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countryList[position]
        holder.countryView.nameTextView.text = country.name
        holder.countryView.nativeNameTextView.text = country.nativeName
        holder.countryView.setOnClickListener {
            onCountryClick(countryList[position].regionalBlockAcronym as ArrayList<String>)
            holder.countryView.isPressed = false
        }
    }

    override fun getItemCount() = countryList.size

    fun setCountryList(countries: List<CountriesViewModel.Country>) {
        countryList = countries
        notifyDataSetChanged()
    }

}