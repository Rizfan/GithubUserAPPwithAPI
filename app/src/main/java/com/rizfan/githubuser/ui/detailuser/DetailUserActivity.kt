package com.rizfan.githubuser.ui.detailuser

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.rizfan.githubuser.R
import com.rizfan.githubuser.data.database.FavoriteUser
import com.rizfan.githubuser.data.response.DetailUserResponse
import com.rizfan.githubuser.databinding.ActivityDetailUserBinding
import com.rizfan.githubuser.ui.ViewModelFactory
import com.rizfan.githubuser.ui.favoriteuser.FavoriteUsersActivity
import com.rizfan.githubuser.ui.favoriteuser.FavoriteUsersViewModel
import com.rizfan.githubuser.ui.main.MainViewModel
import com.rizfan.githubuser.ui.settings.SettingPreferences
import com.rizfan.githubuser.ui.settings.dataStore


class DetailUserActivity : AppCompatActivity() {

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!

    private var favoriteUser: FavoriteUser? = null

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        val pref = SettingPreferences.getInstance(application.dataStore)

        val favoriteUserViewModel by viewModels<FavoriteUsersViewModel> {
            ViewModelFactory.getInstance(this.application, pref)
        }

        val username = intent.getStringExtra(username)
        detailUserViewModel.getDetailUser(username.toString())

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username.toString()

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter


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

        favoriteUser = FavoriteUser()

        detailUserViewModel.detailUser.observe(this) { detailUser ->
            setDetailUserData(detailUser)

            val tabTittle = resources.getStringArray(R.array.tab_titles)
            tabTittle[0] = getString(R.string.followers, detailUser.followers)
            tabTittle[1] = getString(R.string.following, detailUser.following)
            tabTittle[2] = getString(R.string.repository, detailUser.publicRepos)

            TabLayoutMediator(binding.tabs, viewPager) { tab, position ->
                tab.text = tabTittle[position]
            }.attach()
            supportActionBar?.elevation = 0f

            favoriteUser.let {
                favoriteUser?.username = detailUser.login.toString()
                favoriteUser?.avatarUrl = detailUser.avatarUrl.toString()
            }
        }

        favoriteUserViewModel.getFavoriteUserByUsername(username.toString()).observe(this) {
            if (it != null) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                binding.ivFavorite.setOnClickListener {
                    favoriteUserViewModel.deleteFavoriteUser(username.toString())
                }
            } else {
                binding.ivFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                binding.ivFavorite.setOnClickListener {
                    favoriteUserViewModel.setFavoriteUser(favoriteUser!!)
                }
            }
        }

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailUserViewModel.errorMessage.observe(this) {
            showError(it)
        }

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

    private fun setDetailUserData(detailUser: DetailUserResponse) {
        with(binding) {
            tvLogin.text = detailUser.login
            tvNamaLengkap.text = detailUser.name
            ivAvatar.loadImage(detailUser.avatarUrl)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }

    companion object {
        const val username = "username"
    }
}