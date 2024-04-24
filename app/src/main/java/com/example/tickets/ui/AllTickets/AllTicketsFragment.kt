package com.example.tickets.ui.AllTickets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tickets.Adapters.AllTicketsAdapter
import com.example.tickets.R
import com.example.tickets.SharedViewModel
import com.example.tickets.data.AllTickets
import com.example.tickets.data.PriceAllTickets
import com.example.tickets.databinding.FragmentAllTicketsBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class AllTicketsFragment : Fragment() {
    private var _binding: FragmentAllTicketsBinding? = null
    private val adapter = AllTicketsAdapter()
    private val binding get() = _binding!!

    val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllTicketsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAllTicketsFromJson(requireContext())
        binding.imGoBackFromAllTickets.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadAllTicketsFromJson(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.resources.openRawResource(R.raw.tickets)
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                val gson = Gson()
                val jsonObject = JSONObject(jsonString)
                val allTicketsArray = jsonObject.getJSONArray("tickets")
                val allTicketsData = mutableListOf<AllTickets>()
                for (i in 0 until allTicketsArray.length()) {
                    val allTicketsObject = allTicketsArray.getJSONObject(i)
                    val badge = allTicketsObject.optString("badge", "")
                    val priceObject = allTicketsObject.getJSONObject("price")
                    val formattedPrice =
                        NumberFormat.getNumberInstance(Locale.getDefault()).format(
                            priceObject.getInt("value")
                        ).replace(",", " ") + " ₽"
                    val departureDateStr =
                        allTicketsObject.getJSONObject("departure").getString("date")
                    val departureTime = convertDateTimeToTime(departureDateStr)
                    val arrivalDateStr =
                        allTicketsObject.getJSONObject("arrival").getString("date")
                    val arrivalTime = convertDateTimeToTime(arrivalDateStr)
                    val ticketData = AllTickets(
                        R.drawable.rectangle1,
                        badge,
                        PriceAllTickets(formattedPrice),
                        departureTime,
                        allTicketsObject.getJSONObject("departure").getString("airport"),
                        arrivalTime,
                        allTicketsObject.getJSONObject("arrival").getString("airport"),
                        allTicketsObject.optBoolean("has_transfer", false)
                    )
                    allTicketsData.add(ticketData)
                }

                launch(Dispatchers.Main) {
                    adapter.setAlltickets(allTicketsData)
                    binding.rcTickets.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.rcTickets.adapter = adapter
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun convertDateTimeToTime(dateTimeStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return try {
            val date = inputFormat.parse(dateTimeStr)
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
