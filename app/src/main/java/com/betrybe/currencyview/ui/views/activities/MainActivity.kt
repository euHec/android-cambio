package com.betrybe.currencyview.ui.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betrybe.currencyview.R
import com.betrybe.currencyview.common.ApiIdlingResource
import com.betrybe.currencyview.data.api.ApiClientService
import com.betrybe.currencyview.ui.adapters.RateAdapter
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private val apiService = ApiClientService.instance
    private val apikey = "20EyGYInJopKHhX7HzFTsiWv41CbMtdF"
    private val menuDropdown: AutoCompleteTextView by lazy { findViewById(R.id.currency_selection_input_layout) }
    private val loadCurrencyState: MaterialTextView by lazy { findViewById(R.id.load_currency_state) }
    private val selectCurrencyState: MaterialTextView by lazy { findViewById(R.id.select_currency_state) }
    private val waitRatesState: FrameLayout by lazy { findViewById(R.id.waiting_response_state) }
    private val listRecycleView: RecyclerView by lazy { findViewById(R.id.currency_rates_state) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(IO).launch {
            try {
                ApiIdlingResource.increment()
                val responseGetSymbols = apiService.getSymbol()

                if (responseGetSymbols.isSuccessful) {

                    if (responseGetSymbols.body() != null) {

                        val body = responseGetSymbols.body()
                        val symbols = body?.symbols
                        val adapter = symbols?.keys?.let {
                            ArrayAdapter(
                                this@MainActivity,
                                android.R.layout.simple_dropdown_item_1line,
                                it.toList()
                            )
                        }

                        withContext(Main) {

                            loadCurrencyState.visibility = View.GONE
                            selectCurrencyState.visibility = View.VISIBLE

                            menuDropdown.setAdapter(adapter)

                            menuDropdown.setOnItemClickListener {
                                parent, view, position, id ->
                                val rate = parent.getItemAtPosition(position).toString()
                                selectCurrencyState.visibility = View.GONE
                                waitRatesState.visibility = View.VISIBLE
                                CoroutineScope(IO).launch {
                                    try {
                                        ApiIdlingResource.increment()
                                        val responseGetSymbols = apiService.getLatestRates(rate)

                                        if (responseGetSymbols.isSuccessful) {
                                            if (responseGetSymbols.body() != null) {
                                                val body = apiService.getLatestRates(rate)
                                                    .body()?.rates!!
                                                CoroutineScope(Main).launch {
                                                    val adapter = RateAdapter(body)
                                                    listRecycleView.adapter = adapter
                                                    listRecycleView.layoutManager = GridLayoutManager(this@MainActivity, 1)
                                                    waitRatesState.visibility = View.GONE
                                                    listRecycleView.visibility = View.VISIBLE
                                                }
                                            }
                                        }
                                        ApiIdlingResource.decrement()
                                    } catch (e: HttpException) {
                                        ApiIdlingResource.decrement()
                                        Log.e("RETURN-APP", "error: ${e.message}")

                                    }

                                }
                            }
                        }
                    }
                }

                ApiIdlingResource.decrement()
            } catch (e: HttpException) {
                ApiIdlingResource.decrement()
                Log.e("RETURN-APP", "error: ${e.message}")
            }
        }
    }
}
