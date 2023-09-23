package com.rizfan.githubuser.ui.detailuser

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.rizfan.githubuser.R
import com.rizfan.githubuser.data.database.FavoriteUser
import com.rizfan.githubuser.data.response.DetailUserResponse
import com.rizfan.githubuser.databinding.ActivityDetailUserBinding
import com.rizfan.githubuser.ui.ViewModelFactory
import com.rizfan.githubuser.ui.favoriteuser.FavoriteUsersViewModel
import com.rizfan.githubuser.ui.settings.SettingPreferences
import com.rizfan.githubuser.ui.settings.dataStore


class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding

    private var favoriteUser: FavoriteUser? = null


    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val detailViewModel by viewModels<FavoriteUsersViewModel> {
            ViewModelFactory.getInstance(this.application, pref)
        }

        val username = intent.getStringExtra(username)
        detailUserViewModel.getDetailUser(username.toString())

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username.toString()

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        favoriteUser = FavoriteUser()

        detailUserViewModel.detailUser.observe(this) { detailUser ->
            setDetailUserData(detailUser)

            val tabTittle = resources.getStringArray(R.array.tab_titles)
            tabTittle[0] = getString(R.string.followers, detailUser.followers)
            tabTittle[1] = getString(R.string.following, detailUser.following)

            TabLayoutMediator(binding.tabs, viewPager) { tab, position ->
                tab.text = tabTittle[position]
            }.attach()
            supportActionBar?.elevation = 0f

            favoriteUser.let {
                favoriteUser?.username = detailUser.login.toString()
                favoriteUser?.avatarUrl = detailUser.avatarUrl.toString()
            }
        }
        detailViewModel.getFavoriteUserByUsername(username.toString()).observe(this) {
            if (it != null) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                binding.ivFavorite.setOnClickListener {
                    detailViewModel.deleteFavoriteUser(username.toString())
                }
            } else {
                binding.ivFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                binding.ivFavorite.setOnClickListener {
                    detailViewModel.setFavoriteUser(favoriteUser!!)
                }
            }
        }

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailUserViewModel.errorMessage.observe(this) {
            showError(it)
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
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
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