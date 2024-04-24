package com.example.tickets.ui.Aviatickets

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tickets.Adapters.OffersAdapter
import com.example.tickets.R
import com.example.tickets.data.Offers
import com.example.tickets.data.Price
import com.example.tickets.databinding.FragmentAviaticketsBinding
import com.example.tickets.functions.loadLastTown
import com.example.tickets.functions.saveLastTown
import com.example.tickets.ui.bottomsheetfragment.BottomFragment
import com.google.gson.Gson
import kirilica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale


class AviaticketsFragment : Fragment() {
    private var _binding: FragmentAviaticketsBinding? = null
    private val adapter = OffersAdapter()
    private val binding get() = _binding!!
    private val imageList = listOf(
        R.drawable.music1,
        R.drawable.music2,
        R.drawable.music3
    )
    private var indexMusic = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAviaticketsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.edtDeparture.kirilica()
        binding.editArrival.kirilica()
        binding.editArrival.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val bottomFragment = BottomFragment()
                bottomFragment.show(parentFragmentManager, "bottom_fragment_tag")
                val bundle = Bundle()
                bundle.putString("departure_value", binding.edtDeparture.text.toString())
                bottomFragment.arguments = bundle
            }
        }

        val lastDeparture = loadLastTown(requireContext(), "last_departure")
        if (!lastDeparture.isNullOrEmpty()) {
            binding.edtDeparture.setText(lastDeparture)
        }
        binding.edtDeparture.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    saveLastTown(requireContext(), "last_departure", it.toString())
                }
            }
        })


        loadOffersFromJson(requireContext())

        return root
    }


    private fun loadOffersFromJson(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.resources.openRawResource(R.raw.offers)
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                val gson = Gson()
                val jsonObject = JSONObject(jsonString)
                val offersArray = jsonObject.getJSONArray("offers")

                val offersData = mutableListOf<Offers>()

                for (i in 0 until offersArray.length()) {
                    val offerObject = offersArray.getJSONObject(i)
                    val priceObject = offerObject.getJSONObject("price")
                    val formattedPrice ="от "+ NumberFormat.getNumberInstance(Locale.getDefault()).
                    format(priceObject.getInt("value")).replace(",", " ") + " ₽"

                    val offerData = Offers(
                        imageList[indexMusic % imageList.size], // преобразуем в строку, чтобы соответствовать новому типу
                        offerObject.getString("title"),
                        offerObject.getString("town"),
                        Price(formattedPrice)
                    )

                    offersData.add(offerData)
                    indexMusic++
                }

                launch(Dispatchers.Main) {
                    adapter.setOffers(offersData)
                    binding.rcOffers.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.rcOffers.adapter = adapter
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}