package com.rizfan.githubuser.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rizfan.githubuser.data.FavoriteUserRepository
import com.rizfan.githubuser.di.Injection
import com.rizfan.githubuser.ui.favoriteuser.FavoriteUsersViewModel
import com.rizfan.githubuser.ui.main.MainViewModel
import com.rizfan.githubuser.ui.settings.SettingPreferences

class ViewModelFactory private constructor(
    private val repository: FavoriteUserRepository,
    private val pref: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteUsersViewModel::class.java)) {
            return FavoriteUsersViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(repository: Application, pref: SettingPreferences): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(repository), pref)
            }.also { instance = it }
    }
}