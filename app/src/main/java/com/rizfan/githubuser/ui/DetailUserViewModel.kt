package com.rizfan.githubuser.ui

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rizfan.githubuser.data.response.DetailUserResponse
import com.rizfan.githubuser.data.response.ItemsItem
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

    companion object {
        const val TAG = "DetailUserViewModel"
    }

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
                    _detailUser.value = response.body()
                } else {
                    Toast.makeText(null, "Gagal mendapatkan Data User!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(null, "Gagal mendapatkan Data User!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getFollowers(username: String){
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
                    Toast.makeText(null, "Gagal mendapatkan Data Follower!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(null, "Gagal mendapatkan Data Follower!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getFollowings(username: String){
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
                    Toast.makeText(null, "Gagal mendapatkan Data Following!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(null, "Gagal mendapatkan Data Following!", Toast.LENGTH_SHORT).show()
            }
        })
    }

}