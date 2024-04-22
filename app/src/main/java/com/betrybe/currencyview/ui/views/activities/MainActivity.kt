package com.betrybe.currencyview.ui.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.betrybe.currencyview.R
import com.betrybe.currencyview.common.ApiIdlingResource
import com.betrybe.currencyview.data.api.ApiClientService
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val apiService = ApiClientService.instance
    private val apikey = "20EyGYInJopKHhX7HzFTsiWv41CbMtdF"
    private val menuDropdown: AutoCompleteTextView by lazy { findViewById(R.id.currency_selection_input_layout) }
    private val loadCurrencyState: MaterialTextView by lazy { findViewById(R.id.load_currency_state) }
    private val selectCurrencyState: MaterialTextView by lazy { findViewById(R.id.select_currency_state) }
    private val waitRatesState: MaterialTextView by lazy { findViewById(R.id.waiting_response_state) }
    private val listRecycleView: RecyclerView by lazy { findViewById(R.id.currency_rates_state) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("RETURN-APP", "Checkpoint 1")

        CoroutineScope(IO).launch {
            try {
                ApiIdlingResource.increment()
                val responseGetSymbols = apiService.getSymbol()
                Log.i("RETURN-APP", "Checkpoint 2")
                Log.i("RETURN-APP", responseGetSymbols.toString())


                if (responseGetSymbols.isSuccessful) {
                    Log.i("RETURN-APP", "Checkpoint 3")

                    if (responseGetSymbols.body() != null) {
                        Log.i("RETURN-APP", "Checkpoint 4")

                        val body = responseGetSymbols.body()
                        val symbols = body?.symbols
                        Log.i("RETURN-APP", symbols.toString())
                        val adapter = symbols?.keys?.let {
                            ArrayAdapter(
                                this@MainActivity,
                                android.R.layout.simple_dropdown_item_1line,
                                it.toList()
                            )
                        }
                        Log.i("RETURN-APP", "Checkpoint 5")

                        withContext(Main) {
                            Log.i("RETURN-APP", "Checkpoint 6")
                            loadCurrencyState.visibility = View.GONE
                            selectCurrencyState.visibility = View.VISIBLE
                            menuDropdown.setAdapter(adapter)
                        }
                        Log.i("RETURN-APP", "Checkpoint 7")
                    }
                }

                ApiIdlingResource.decrement()
            } catch (e: HttpException) {
                ApiIdlingResource.decrement()
                Log.e("RETURN-APP", "app")
            } catch (e: IOException) {
                ApiIdlingResource.decrement()
            }
        }
    }
}
