package com.example.myappgithub.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myappgithub.data.response.ItemsItem

class MainViewModel : ViewModel() {
    private val _items = MutableLiveData<ItemsItem>()
    val items : LiveData<ItemsItem> = _items
    private val _itemsListUser = MutableLiveData<List<ItemsItem>>()
    val itemsListUser: LiveData<List<ItemsItem>> = _itemsListUser

    companion object{
        private val _isLoading = MutableLiveData<Boolean>()
        val isLoading: LiveData<Boolean> = _isLoading
        private const val TAG = "MainActivity"
        private const val type = "login"
    }
}