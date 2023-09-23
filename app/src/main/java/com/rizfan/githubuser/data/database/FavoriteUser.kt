package com.rizfan.githubuser.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Entity
@Parcelize
data class FavoriteUser (

    @PrimaryKey(autoGenerate = false)
    var username: String="",
    var avatarUrl: String? = null,

):Parcelable