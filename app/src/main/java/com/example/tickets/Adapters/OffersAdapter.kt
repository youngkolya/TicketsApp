package com.example.tickets.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tickets.R
import com.example.tickets.data.Offers
import com.example.tickets.databinding.OffersItemBinding



class OffersAdapter:RecyclerView.Adapter<OffersAdapter.OffersHolder>() {
    private val offersList = mutableListOf<Offers>()

    fun setOffers(offers: List<Offers>) {
        offersList.clear()
        offersList.addAll(offers)
        notifyDataSetChanged()
    }
    class OffersHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = OffersItemBinding.bind(item)
        fun bind(offers: Offers) = with(binding) {
            imOffers.setImageResource(offers.imageId)
            tvOffersTitle.text = offers.title
            tvOffersTown.text = offers.town
            tvOffersPrice.text = offers.price.value
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offers_item, parent, false)
        return OffersHolder(view)
    }

    override fun getItemCount(): Int {
        return offersList.size
    }

    override fun onBindViewHolder(holder: OffersHolder, position: Int) {
        val offer = offersList[position]
        holder.bind(offer)
    }

}