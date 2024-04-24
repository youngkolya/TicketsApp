package com.example.tickets.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tickets.R
import com.example.tickets.data.OffersTicket
import com.example.tickets.databinding.OffersTicketsItemBinding

class OffersTicketAdapter : RecyclerView.Adapter<OffersTicketAdapter.OffersTicketHolder>() {
    private val offersTicketList = mutableListOf<OffersTicket>()

    fun setOffersTicket(offersTicket: List<OffersTicket>) {
        offersTicketList.clear()
        offersTicketList.addAll(offersTicket)
        notifyDataSetChanged()
    }

    class OffersTicketHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = OffersTicketsItemBinding.bind(item)

        fun bind(offersTicket: OffersTicket) {
            with(binding) {
                imOffersTicket.setImageResource(offersTicket.imageId)
                tvNameAvialine.text = offersTicket.title
                tvTimeAvialine.text = offersTicket.timeRange.joinToString("")
                tvPrice.text= offersTicket.priceOffersTicket.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersTicketHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offers_tickets_item, parent, false)
        return OffersTicketHolder(view)
    }

    override fun getItemCount(): Int {
        return offersTicketList.size
    }

    override fun onBindViewHolder(holder: OffersTicketHolder, position: Int) {
        val offersTicket = offersTicketList[position]
        holder.bind(offersTicket)
    }
}
