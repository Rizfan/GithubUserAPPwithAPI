package com.rizfan.githubuser.ui

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rizfan.githubuser.data.response.GithubResponse
import com.rizfan.githubuser.data.response.ItemsItem
import com.rizfan.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>?>()
    val listUser: MutableLiveData<List<ItemsItem>?> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        const val TAG = "MainViewModel"
        private const val Q = "Rizfan"
    }

    init {
        getUser()
    }

    private fun getUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(Q)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    Toast.makeText(null, "Gagal mendapatkan Data User!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(null, "Gagal mendapatkan Data User!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun searchUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    Toast.makeText(null, "Gagal mendapatkan Data User!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(null, "Gagal mendapatkan Data User!", Toast.LENGTH_SHORT).show()
            }
        })
    }

}