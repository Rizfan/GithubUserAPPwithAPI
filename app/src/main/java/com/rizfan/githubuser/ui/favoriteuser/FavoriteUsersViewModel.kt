package com.rizfan.githubuser.ui.favoriteuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rizfan.githubuser.data.FavoriteUserRepository
import com.rizfan.githubuser.data.database.FavoriteUser

class FavoriteUsersViewModel (private val repository: FavoriteUserRepository): ViewModel() {

    fun getFavoriteUsers() : LiveData<List<FavoriteUser>> = repository.getFavoriteUsers()


    fun setFavoriteUser(favoriteUser: FavoriteUser) {
        repository.setFavoriteUser(favoriteUser)
    }

    fun getFavoriteUserByUsername(username: String):LiveData<FavoriteUser> = repository.getFavoriteUserByUsername(username)


    fun deleteFavoriteUser(username: String) {
        repository.deleteFavoriteUser(username)
    }

}

