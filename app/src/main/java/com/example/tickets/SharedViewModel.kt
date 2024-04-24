package com.example.tickets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class SharedViewModel:ViewModel() {
    val textFromBottomSheet1LiveData = MutableLiveData<String>()
    val textFromBottomSheet2LiveData = MutableLiveData<String>()
    val textFromSelect1LiveData = MutableLiveData<String>()
    val textFromSelect2LiveData= MutableLiveData<String>()
    fun setTextFromBottomSheet1(text: String) {
        textFromBottomSheet1LiveData.value = text
    }

    fun setTextFromBottomSheet2(text: String) {
        textFromBottomSheet2LiveData.value = text
    }
    fun setTextFromSelect1(text: String){
        textFromSelect1LiveData.value = text
    }
    fun setTextFromSelect2(text: String){
        textFromSelect2LiveData.value = text
    }

}