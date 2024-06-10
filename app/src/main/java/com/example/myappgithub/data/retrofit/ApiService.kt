package com.example.myappgithub.data.retrofit

import com.example.myappgithub.data.response.DetailUserResponse
import com.example.myappgithub.data.response.GithubResponse
import com.example.myappgithub.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
        @Headers("Authorization: token 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")
        @GET("search/users")
        fun getItemsItem(
            @Query("q") q: String
        ): Call<GithubResponse>

        @GET("users/{username}")
        fun getDetailUser(
            @Path("username") username: String
        ): Call<DetailUserResponse>

        @GET("users/{username}/followers")
        fun getFollowers(
            @Path("username")username: String
        ): Call <List<ItemsItem>>

        @GET("users/{username}/following")
        fun getFollowing(
            @Path("username")username: String
        ): Call <List<ItemsItem>>
}