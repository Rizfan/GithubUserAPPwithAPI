package com.rizfan.githubuser.data.retrofit

import com.rizfan.githubuser.data.response.DetailUserResponse
import com.rizfan.githubuser.data.response.GithubResponse
import com.rizfan.githubuser.data.response.ItemsItem
import com.rizfan.githubuser.data.response.RepoResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getUsers(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/repos")
    fun getRepo(
        @Path("username") username: String
    ): Call<List<RepoResponseItem>>
}