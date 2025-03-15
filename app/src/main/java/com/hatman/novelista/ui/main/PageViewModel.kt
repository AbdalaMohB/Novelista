package com.hatman.novelista.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()

    // TODO: change the data here to return titles and dates 
    val text: LiveData<Int> = _index.map {
        it

    }
    fun setIndex(index: Int) {
        _index.value = index

    }
}