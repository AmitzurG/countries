package com.app.countries.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class CountriesViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val COUNTRIES_REQUEST_TAG = "countriesRequestTag"
    }

    override fun onCleared() {
        queue?.cancelAll(COUNTRIES_REQUEST_TAG)
    }

    val countries = MutableLiveData<List<Country>>()
    val countryNeighbors = MutableLiveData<List<Country>>()

    private val queue by lazy { Volley.newRequestQueue(getApplication()) }

    fun getCountries() {
        val url = "https://restcountries.eu/rest/v2/all?fields=name;nativeName;area;regionalBlocs"
        val countriesJsonArray = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener {
                countries.value = getCountryList(it)
            },
            Response.ErrorListener {
                // TODO: Handle error
            }
        )
        countriesJsonArray.tag = COUNTRIES_REQUEST_TAG
        queue.add(countriesJsonArray)
    }

//    fun getCountryNeighbors(vararg regionalAcronyms: String) {
//        val countryList = ArrayList<Country>()
//        for (acronym in regionalAcronyms) {
//            val url = "https://restcountries.eu/rest/v2/regionalbloc/$acronym"
//        }
//
//    }

    fun getCountryNeighbors(regionalAcronym: String) {
        val url = "https://restcountries.eu/rest/v2/regionalbloc/$regionalAcronym?fields=name;nativeName;area;regionalBlocs"
        val countriesJsonArray = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener {
                countryNeighbors.value = getCountryList(it)
            },
            Response.ErrorListener {
                // TODO: Handle error
            }
        )
        countriesJsonArray.tag = COUNTRIES_REQUEST_TAG
        queue.add(countriesJsonArray)
    }

    private fun getCountryList(countriesJsonArray: JSONArray): List<Country> {
        val countryList = ArrayList<Country>(300)
        try {
            val length = countriesJsonArray.length()
            for (i in 0 until length) {
                val jsonObject = countriesJsonArray.getJSONObject(i)
                if (jsonObject != null) {
                    val name = jsonObject.getString("name")
                    val nativeName = jsonObject.getString("nativeName")
                    val area = if (jsonObject.has("area")) {
                        jsonObject.getDouble("area")
                    } else 0.0
                    val regionalBlocs = jsonObject.getJSONArray("regionalBlocs")
                    val regionalAcronym = ArrayList<String>()
                    for (j in 0 until regionalBlocs.length()) {
                        val acronym: String = regionalBlocs.getJSONObject(j).getString("acronym")
                        regionalAcronym.add(acronym)
                    }
                    countryList.add(Country(name, nativeName, area, regionalAcronym))
                }
            }
        } catch (e: JSONException) {
            Log.e("Countries", "CountriesViewModel.getCountryList - JSONException, error=${e.message}")
        }
        return countryList
    }

    data class Country(val name: String, val nativeName: String, val area: Double, val regionalBlockAcronym: List<String>)
}

class CountriesViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CountriesViewModel(application) as T
    }
}