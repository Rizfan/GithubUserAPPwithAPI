package com.rizfan.githubuser.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.rizfan.githubuser.R
import com.rizfan.githubuser.data.response.DetailUserResponse
import com.rizfan.githubuser.databinding.ActivityDetailUserBinding


class DetailUserActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDetailUserBinding

    companion object {
        const val username = "username"
    }

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailUserViewModel::class.java)

        val username = intent.getStringExtra(username)
        detailUserViewModel.getDetailUser(username.toString())

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username.toString()

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        detailUserViewModel.detailUser.observe(this) { detailUser ->
            setDetailUserData(detailUser)

            val tabTittle = resources.getStringArray(R.array.tab_titles)
            tabTittle[0] = getString(R.string.followers, detailUser.followers)
            tabTittle[1] = getString(R.string.following, detailUser.following)

            TabLayoutMediator(binding.tabs, viewPager) { tab, position ->
                tab.text = tabTittle[position]
            }.attach()
            supportActionBar?.elevation = 0f
        }

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }


    }

    private fun setDetailUserData(detailUser: DetailUserResponse) {
        with(binding){
            tvLogin.text = detailUser.login
            tvNamaLengkap.text = detailUser.name
            Glide.with(this@DetailUserActivity)
                .load(detailUser.avatarUrl)
                .into(ivAvatar)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = android.view.View.VISIBLE
        } else {
            binding.progressBar.visibility = android.view.View.GONE
        }
    }
}