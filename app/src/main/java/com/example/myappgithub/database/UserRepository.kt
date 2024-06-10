package com.example.myappgithub.database

import androidx.lifecycle.LiveData

class UserRepository(private val favoriteUserDao: FavoriteUserDao) {

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return favoriteUserDao.getAllFavoriteUsers()
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return favoriteUserDao.getFavoriteUserByUsername(username)
    }

    fun insert(favoriteUser: FavoriteUser) {
        favoriteUserDao.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        favoriteUserDao.delete(favoriteUser)
    }
}