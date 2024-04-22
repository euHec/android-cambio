package com.betrybe.currencyview.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.betrybe.currencyview.R
import com.google.android.material.textview.MaterialTextView

class RateAdapter(private val listCurrencyRates: Map<String, Double>) : Adapter<RateAdapter.RateViewHolder>() {
    val currencyRateskeys: List<String> = listCurrencyRates.keys.toList()

    class RateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<MaterialTextView>(R.id.name_rate)
        val detail = view.findViewById<MaterialTextView>(R.id.detail_rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_rate_layout, parent, false)
        return RateViewHolder(view)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {

        val rate = currencyRateskeys[position].toString()
        holder.name.text = rate
        holder.detail.text = listCurrencyRates[rate].toString()
    }

    override fun getItemCount(): Int = listCurrencyRates.size

}
