package com.example.tickets.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tickets.R
import com.example.tickets.data.AllTickets
import com.example.tickets.databinding.AllTicketsItemBinding

class AllTicketsAdapter:RecyclerView.Adapter<AllTicketsAdapter.AllticketsHolder>() {
    private val allTicketsList = mutableListOf<AllTickets>()
    fun setAlltickets(allTickets: List<AllTickets>){
        allTicketsList.clear()
        allTicketsList.addAll(allTickets)
        notifyDataSetChanged()
    }
    class AllticketsHolder(item:View):RecyclerView.ViewHolder(item){
        val binding = AllTicketsItemBinding.bind(item)
        fun bind(allTickets: AllTickets) = with(binding){
            imTicketSall.setImageResource(allTickets.imageId)
            chipBadge.text = allTickets.badge
            tvPriceAll.text= allTickets.price.value
            tvTimeStart.text = allTickets.departureDate
            tvStartAirport.text= allTickets.departureAirport
            tvTimeFinish.text = allTickets.arrivalDate
            tvFinishAirport.text=allTickets.arrivalAirport
            if (allTickets.transfer) {
                tvTransfer.visibility = View.VISIBLE
            } else {
                tvTransfer.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllticketsHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.all_tickets_item, parent, false)
        return AllticketsHolder(view)
    }

    override fun onBindViewHolder(
        holder: AllticketsHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val currentTicket = allTicketsList[position]
        holder.binding.chipBadge.apply {
            if (currentTicket.badge.isNullOrEmpty()){
                visibility = View.GONE
            }else{
                visibility=View.VISIBLE
                text=currentTicket.badge
            }
        }
    }

    override fun getItemCount(): Int {
        return allTicketsList.size
    }

    override fun onBindViewHolder(holder: AllticketsHolder, position: Int) {
        val ticket = allTicketsList[position]
        holder.bind(ticket)
    }
}