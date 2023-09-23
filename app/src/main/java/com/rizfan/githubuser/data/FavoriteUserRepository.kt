package com.rizfan.githubuser.data

import androidx.lifecycle.LiveData
import com.rizfan.githubuser.data.database.FavoriteUser
import com.rizfan.githubuser.data.database.FavoriteUserDao
import com.rizfan.githubuser.data.utils.AppExecutors

class FavoriteUserRepository private constructor(
    private var favoriteUserDao: FavoriteUserDao,
    private val appExecutors: AppExecutors
){

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>>{
        return favoriteUserDao.getFavoriteUsers()
    }

    fun setFavoriteUser(favoriteUser: FavoriteUser){
        appExecutors.diskIO.execute {
            favoriteUserDao.insert(favoriteUser)
        }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return favoriteUserDao.getFavoriteUserByUsername(username)
    }

    fun deleteFavoriteUser(username: String){
        appExecutors.diskIO.execute {
            favoriteUserDao.delete(username)
        }
    }

    companion object{
        @Volatile
        private var instance: FavoriteUserRepository? = null

        fun getInstance(dao: FavoriteUserDao, appExecutors: AppExecutors): FavoriteUserRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteUserRepository(dao, appExecutors)
            }.also { instance = it }
    }
}