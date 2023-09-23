package com.rizfan.githubuser.di

import android.content.Context
import com.rizfan.githubuser.data.FavoriteUserRepository
import com.rizfan.githubuser.data.database.FavoriteUserRoomDatabase
import com.rizfan.githubuser.data.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteUserRepository {
        val database = FavoriteUserRoomDatabase.getDatabase(context)
        val dao = database.favoriteUserDao()
        val appExecutors = AppExecutors()
        return FavoriteUserRepository.getInstance(dao, appExecutors)
    }
}