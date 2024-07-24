package com.rizfan.githubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizfan.githubuser.R
import com.rizfan.githubuser.data.response.ItemsItem
import com.rizfan.githubuser.databinding.ActivityMainBinding
import com.rizfan.githubuser.ui.ViewModelFactory
import com.rizfan.githubuser.ui.favoriteuser.FavoriteUsersActivity
import com.rizfan.githubuser.ui.settings.SettingPreferences
import com.rizfan.githubuser.ui.settings.dataStore

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel by viewModels<MainViewModel> {
            ViewModelFactory.getInstance(this.application, pref)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        var theme = false

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            theme = isDarkModeActive
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.topAppBar.menu.findItem(R.id.mnTheme).setIcon(R.drawable.baseline_brightness_2_24)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.topAppBar.menu.findItem(R.id.mnTheme).setIcon(R.drawable.ic_sun)
            }
        }

        mainViewModel.listUser.observe(this) { listUser ->
            setUserList(listUser)

        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.errorMessage.observe(this) {
            if (it != null) {
                showError(it)
            } else {
                binding.tvError.visibility = View.GONE
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.searchUser(searchBar.text.toString())
                    false
                }

        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mnFavorite -> {
                    val intentFavorite =
                        Intent(this@MainActivity, FavoriteUsersActivity::class.java)
                    startActivity(intentFavorite)
                    true
                }
                R.id.mnTheme -> {
                    mainViewModel.saveThemeSetting(!theme)
                    true
                }
                else -> false
            }
        }
    }

    private fun setUserList(listUser: List<ItemsItem>?) {
        val adapter = UserAdapter()
        adapter.submitList(listUser)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        binding.tvError.visibility = View.VISIBLE
        binding.tvError.text = errorMessage
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}