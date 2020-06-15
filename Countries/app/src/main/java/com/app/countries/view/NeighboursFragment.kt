package com.app.countries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_neighbours.*
import java.lang.StringBuilder

class NeighboursFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_neighbours, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadCountryNeighbours()
    }

    private fun loadCountryNeighbours() {
        val acronyms = arguments?.get(CountriesFragment.regionalAcronymKey) as ArrayList<*>
        if (!acronyms.isNullOrEmpty()) {
            notAvailableText(false)
            val acronymsStringBuilder = StringBuilder()
            for (i in 0 until acronyms.size) {
                acronymsStringBuilder.append("${acronyms[i]}")
                if (i < acronyms.size - 1) {
                    acronymsStringBuilder.append(";")
                }
            }
            (activity as CountriesActivity).viewModel.getCountryNeighbors(acronymsStringBuilder.toString())
            (activity as CountriesActivity).viewModel.countryNeighbors.observe(viewLifecycleOwner, Observer {
                countryNeighboursRecyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(this@NeighboursFragment.context)
                    adapter = CountriesRecyclerViewAdapter(it)
                }
            })
        } else {
            notAvailableText()
        }
    }

    private fun notAvailableText(visible: Boolean = true) {
        countryNeighboursRecyclerView.visibility = if(visible) View.GONE else View.VISIBLE
        notAvailableTextView.visibility = if(visible) View.VISIBLE else View.GONE
    }
}