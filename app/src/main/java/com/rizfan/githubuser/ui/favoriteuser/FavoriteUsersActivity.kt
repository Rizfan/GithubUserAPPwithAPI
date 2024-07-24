package com.rizfan.githubuser.ui.favoriteuser

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizfan.githubuser.R
import com.rizfan.githubuser.data.response.ItemsItem
import com.rizfan.githubuser.databinding.ActivityFavoriteUsersBinding
import com.rizfan.githubuser.ui.ViewModelFactory
import com.rizfan.githubuser.ui.main.MainViewModel
import com.rizfan.githubuser.ui.settings.SettingPreferences
import com.rizfan.githubuser.ui.settings.dataStore

class FavoriteUsersActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteUsersBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val favoriteViewModel by viewModels<FavoriteUsersViewModel> {
            ViewModelFactory.getInstance(this.application, pref)
        }
        val themeViewModel by viewModels<MainViewModel> {
            ViewModelFactory.getInstance(this.application, pref)
        }

        var theme = false

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            theme = isDarkModeActive
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.topAppBar.menu.findItem(R.id.mnTheme).setIcon(R.drawable.baseline_brightness_2_24)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.topAppBar.menu.findItem(R.id.mnTheme).setIcon(R.drawable.ic_sun)
            }
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
            if (listUser.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
        }

        binding.rvUser.adapter = adapter

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mnFavorite -> {
                    val intentFavorite =
                        Intent(this, FavoriteUsersActivity::class.java)
                    startActivity(intentFavorite)
                    true
                }
                R.id.mnTheme -> {
                    themeViewModel.saveThemeSetting(!theme)
                    true
                }
                else -> false
            }
        }
    }
}