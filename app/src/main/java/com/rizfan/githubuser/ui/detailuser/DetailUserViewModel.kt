package com.rizfan.githubuser.ui.detailuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rizfan.githubuser.data.response.DetailUserResponse
import com.rizfan.githubuser.data.response.ItemsItem
import com.rizfan.githubuser.data.response.RepoResponseItem
import com.rizfan.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: MutableLiveData<List<ItemsItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: MutableLiveData<List<ItemsItem>> = _listFollowing

    private val _listRepo = MutableLiveData<List<RepoResponseItem>>()
    val listRepo: MutableLiveData<List<RepoResponseItem>> = _listRepo

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() == null) {
                        _errorMessage.value = "Gagal mendapatkan Data User!"
                    } else {
                        _detailUser.value = response.body()
                    }
                } else {
                    _errorMessage.value = "Gagal mendapatkan Data User!"
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Gagal mendapatkan Data User!"
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowers.postValue(response.body())
                } else {
                    _errorMessage.value = "Gagal mendapatkan Data Follower!"
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Gagal mendapatkan Data Follower!"
            }
        })
    }

    fun getFollowings(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowing.postValue(response.body())
                } else {
                    _errorMessage.value = "Gagal mendapatkan Data Following!"
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Gagal mendapatkan Data Following!"
            }
        })
    }

    fun getRepo(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRepo(username)
        client.enqueue(object : Callback<List<RepoResponseItem>> {
            override fun onResponse(
                call: Call<List<RepoResponseItem>>,
                response: Response<List<RepoResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listRepo.postValue(response.body())
                } else {
                    _errorMessage.value = "Gagal mendapatkan Data Repo!"
                }
            }

            override fun onFailure(call: Call<List<RepoResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Gagal mendapatkan Data Repo!"
            }
        })
    }
}