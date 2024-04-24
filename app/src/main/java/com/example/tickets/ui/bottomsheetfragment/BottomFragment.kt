package com.example.tickets.ui.bottomsheetfragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.tickets.R
import com.example.tickets.SharedViewModel
import com.example.tickets.databinding.BottomSheetBinding
import com.example.tickets.ui.SearchSelect.SearchSelect
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kirilica




class BottomFragment: BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetBinding
    val sharedViewModel:SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
        binding = BottomSheetBinding.bind(inflater.inflate(R.layout.bottom_sheet, container))

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let { bottomSheet ->
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edtDepartureFormBottom.kirilica()
        binding.editArrivalFromBottom.kirilica()
        binding.edtDepartureFormBottom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                checkFieldsAndNavigate()
            }
        })
        binding.editArrivalFromBottom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                checkFieldsAndNavigate()
            }
        })

        val departureValue = arguments?.getString("departure_value") ?: ""
        binding.edtDepartureFormBottom.setText(departureValue)

        binding.editArrivalFromBottom.setOnTouchListener { _, event ->
            val drawableRight = 2
            if (event.action == MotionEvent.ACTION_UP &&
                event.rawX >= binding.editArrivalFromBottom.right - binding.editArrivalFromBottom.compoundDrawables[drawableRight].bounds.width()
            ) {
                binding.editArrivalFromBottom.text.clear()
                return@setOnTouchListener true
            }
            false
        }
        binding.editArrivalFromBottom.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkFieldsAndNavigate()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun checkFieldsAndNavigate() {
        val editTextDeparture = binding.edtDepartureFormBottom.text.toString()
        val editTextArrival = binding.editArrivalFromBottom.text.toString()

        if (!TextUtils.isEmpty(editTextDeparture) && !TextUtils.isEmpty(editTextArrival)) {
            sharedViewModel.setTextFromBottomSheet1(editTextDeparture)
            sharedViewModel.setTextFromBottomSheet2(editTextArrival)
            findNavController().navigate(R.id.search_select)
        }
    }
}

