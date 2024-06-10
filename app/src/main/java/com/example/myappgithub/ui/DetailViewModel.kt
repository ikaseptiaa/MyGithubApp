package com.example.myappgithub.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myappgithub.data.response.DetailUserResponse
import com.example.myappgithub.data.response.ItemsItem
import com.example.myappgithub.data.retrofit.ApiConfig
import com.example.myappgithub.database.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    companion object {
        private const val TAG = "DetailViewModel"
    }

    private val _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    fun getUserDetail(username: String){
        _isLoading.value = true
        ApiConfig.getApiService()
            .getDetailUser(username)
            .enqueue(object : Callback<DetailUserResponse>{
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        _userDetail.value = response.body()
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    val errorMessage = "Ada kesalahan: ${t.message}"
                    Log.e(TAG, "onFailure :${t.message}")
                }
            })
    }

    fun getFollowing(username: String){
        _isLoading.value = true
        ApiConfig.getApiService()
            .getFollowing(username)
            .enqueue(object : Callback<List<ItemsItem>>{
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        _following.value = response.body()
                    }
                }
                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    val errorMessage = "Ada Kesalahan: ${t.message}"
                    Log.e(TAG, "onFailure :${t.message}")
                }

            })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        ApiConfig.getApiService()
            .getFollowers(username)
            .enqueue(object : Callback<List<ItemsItem>>{
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        _followers.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    val errorMessage = "Ada kesalahan: ${t.message}"
                    Log.e(TAG,"onFailure :${t.message}")
                }
            })
    }
}