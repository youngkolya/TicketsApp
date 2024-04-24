package com.example.tickets.ui.SearchSelect

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tickets.Adapters.OffersTicketAdapter
import com.example.tickets.R
import com.example.tickets.SharedViewModel
import com.example.tickets.data.OffersTicket
import com.example.tickets.databinding.SearchSelectCountryBinding
import com.example.tickets.ui.bottomsheetfragment.BottomFragment
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import setupDatePicker
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class SearchSelect:Fragment() {
    private var _binding: SearchSelectCountryBinding?=null
    private val adapter = OffersTicketAdapter()
    private val binding get() = _binding!!

    val sharedViewModel: SharedViewModel by viewModels()
    private val imageList = listOf(
        R.drawable.rectangle1,
        R.drawable.rectangle2,
        R.drawable.rectangle3
    )

    private var indexRectangle = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = SearchSelectCountryBinding.inflate(inflater, container, false)
        val root:View = binding.root
        return  root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel.textFromBottomSheet1LiveData.observe(viewLifecycleOwner) { text ->
            binding.tvFromCountry.text = text
        }

        sharedViewModel.textFromBottomSheet2LiveData.observe(viewLifecycleOwner) { text ->
            binding.tvToCountry.text = text
        }
        Log.d("MyLog", "$binding.tvToCountry.text")
        binding.btGoToAllTickets.setOnClickListener {
           findNavController().navigate(R.id.AllTicketsFragment)
        }
        binding.imGoBackFromSelect.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.imageView7.setOnClickListener {
            val tempText = binding.tvFromCountry.text
            binding.tvFromCountry.text = binding.tvToCountry.text
            binding.tvToCountry.text = tempText
        }


        loadOffersTicketFromJson(requireContext())
        setupDatePicker(requireContext(), binding.btDate)
        binding.btGoBack.setOnClickListener { setupDatePicker(requireContext(), binding.btGoBack) }
    }
    private fun loadOffersTicketFromJson(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.resources.openRawResource(R.raw.offers_tickets)
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                val jsonObject = JSONObject(jsonString)
                val offersTicketArray = jsonObject.getJSONArray("tickets_offers")
                val offersTicketData = mutableListOf<OffersTicket>()

                for (i in 0 until offersTicketArray.length()) {
                    val offersTicketObject = offersTicketArray.getJSONObject(i)
                    val priceObject = offersTicketObject.getJSONObject("price")
                    val formattedPrice = NumberFormat.getNumberInstance(Locale.getDefault()).format(
                        priceObject.getInt("value")
                    ).replace(",", " ")+ " ₽"

                    val timeRangeList = mutableListOf<String>()
                    val timeRangeArray = offersTicketObject.getJSONArray("time_range")
                    for (j in 0 until timeRangeArray.length()) {
                        val time = timeRangeArray.getString(j)
                        val formattedTime = StringBuilder(time)
                        if (j < timeRangeArray.length() - 1) {
                            formattedTime.append(" ")
                        }
                        timeRangeList.add(formattedTime.toString())
                    }

                    val offerTicketData = OffersTicket(
                        imageList[indexRectangle % imageList.size],
                        offersTicketObject.getString("title"),
                        timeRangeList,
                        offersTicketObject.getJSONObject("price").getString("value") + " ₽"
                    )
                    offersTicketData.add(offerTicketData)
                    indexRectangle++
                }


                withContext(Dispatchers.Main) {
                    adapter.setOffersTicket(offersTicketData)
                    binding.rcOffersTickets.layoutManager =
                        LinearLayoutManager(context)
                    binding.rcOffersTickets.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }







}