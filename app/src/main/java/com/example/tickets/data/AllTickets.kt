package com.example.tickets.data

data class AllTickets(val  imageId:Int, val badge: String?,
                      val price: PriceAllTickets, val departureDate: String, val departureAirport: String,
                      val arrivalDate:String, val arrivalAirport:String, val transfer: Boolean)
data class PriceAllTickets(val value:String)
