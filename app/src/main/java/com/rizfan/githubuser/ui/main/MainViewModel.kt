package com.rizfan.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rizfan.githubuser.data.response.GithubResponse
import com.rizfan.githubuser.data.response.ItemsItem
import com.rizfan.githubuser.data.retrofit.ApiConfig
import com.rizfan.githubuser.ui.settings.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>?>()
    val listUser: MutableLiveData<List<ItemsItem>?> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    companion object {
        private const val Q = "Rizfan"
    }

    init {
        getUser()
    }

    private fun getUser() {
        _isLoading.value = true
        _errorMessage.value = null
        val client = ApiConfig.getApiService().getUsers(Q)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.items?.size == 0) {
                        _errorMessage.value = "User tidak ditemukan!"
                        _listUser.value = null
                    } else {
                        _listUser.value = response.body()?.items
                    }
                } else {
                    _errorMessage.value = "Gagal mendapatkan Data User!"
                    _listUser.value = null
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _listUser.value = null
                _errorMessage.value = "Gagal mendapatkan Data User!"
            }
        })
    }

    fun searchUser(query: String) {
        _isLoading.value = true
        _errorMessage.value = null
        val client = ApiConfig.getApiService().getUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.items?.size == 0) {
                        _errorMessage.value = "User tidak ditemukan!"
                        _listUser.value = null
                    } else {
                        _listUser.value = response.body()?.items
                    }
                } else {
                    _errorMessage.value = "Gagal mendapatkan Data User!"
                    _listUser.value = null
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Gagal mendapatkan Data User!"
                _listUser.value = null
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}