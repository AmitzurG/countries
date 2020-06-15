package com.app.countries.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.countries.viewmodel.CountriesViewModel
import kotlinx.android.synthetic.main.fragment_countries.*

class CountriesFragment : Fragment() {

    companion object {
        private const val DEC = "decend"
        private const val ANC = "ancend"

        const val regionalAcronymKey = "regionalAcronym"
    }

    private lateinit var countries: List<CountriesViewModel.Country>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadCountriesList()
        setSortByNameButton()
        setSortByAreaButton()
    }

    private fun loadCountriesList() {
        (activity as CountriesActivity).viewModel.getCountries()
        (activity as CountriesActivity).viewModel.countries.observe(viewLifecycleOwner, Observer {
            countries = it
            countriesRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@CountriesFragment.context)
                adapter = CountriesRecyclerViewAdapter(it) {
                    val args = Bundle()
                    args.putStringArrayList(regionalAcronymKey, it)
                    findNavController().navigate(R.id.action_countriesFragment_to_neighboursFragment, args)
                }
            }
        })
    }

    private fun setSortByNameButton() {
        nameSortButton.tag = ANC
        nameSortButton.setOnClickListener {
            val nameComparator = if (nameSortButton.tag == ANC) {
                Comparator { country1: CountriesViewModel.Country, country2: CountriesViewModel.Country ->
                    country1.name.compareTo(country2.name)
                }
            } else {
                Comparator { country1: CountriesViewModel.Country, country2: CountriesViewModel.Country ->
                    country2.name.compareTo(country1.name)
                }
            }
            setSortButton(nameSortButton)
            (countriesRecyclerView.adapter as CountriesRecyclerViewAdapter).setCountryList(countries.sortedWith(nameComparator))
        }
    }

    private fun setSortByAreaButton() {
        areaSortButton.tag = ANC
        areaSortButton.setOnClickListener {
            val areaComparator = if (areaSortButton.tag == ANC) {
                Comparator { country1: CountriesViewModel.Country, country2: CountriesViewModel.Country ->
                    (country1.area - country2.area).toInt()
                }
            } else {
                Comparator { country1: CountriesViewModel.Country, country2: CountriesViewModel.Country ->
                    (country2.area - country1.area).toInt()
                }
            }
            setSortButton(areaSortButton)
            (countriesRecyclerView.adapter as CountriesRecyclerViewAdapter).setCountryList(countries.sortedWith(areaComparator))
        }
    }

    private fun setSortButton(button: Button) = if (button.tag == ANC) {
        button.tag = DEC
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_arrow_upward_24, 0, 0, 0)
    } else {
        button.tag = ANC
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_arrow_downward_24, 0, 0, 0)
    }
}