package com.example.myappgithub.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myappgithub.database.FavoriteUser
import com.example.myappgithub.database.UserRepository

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return repository.getAllFavoriteUsers()
    }
}