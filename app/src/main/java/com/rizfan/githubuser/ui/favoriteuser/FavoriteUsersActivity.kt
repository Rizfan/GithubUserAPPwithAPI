package com.rizfan.githubuser.ui.favoriteuser

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizfan.githubuser.data.response.ItemsItem
import com.rizfan.githubuser.databinding.ActivityFavoriteUsersBinding
import com.rizfan.githubuser.ui.ViewModelFactory
import com.rizfan.githubuser.ui.settings.SettingPreferences
import com.rizfan.githubuser.ui.settings.dataStore

class FavoriteUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUsersBinding

    private lateinit var adapter: FavoriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val favoriteViewModel by viewModels<FavoriteUsersViewModel> {
            ViewModelFactory.getInstance(this.application, pref)
        }

        adapter = FavoriteAdapter()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        favoriteViewModel.getFavoriteUsers().observe(this) { listUser ->
            val user = ArrayList<ItemsItem>()
            listUser.map {
                user.add(ItemsItem(login = it.username, avatarUrl = it.avatarUrl))
            }
            adapter.setUserList(user)
        }

        binding.rvUser.adapter = adapter

    }
}