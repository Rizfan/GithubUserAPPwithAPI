package com.rizfan.githubuser.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizfan.githubuser.R
import com.rizfan.githubuser.data.response.ItemsItem
import com.rizfan.githubuser.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFollowersBinding.bind(view)


        val detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailUserViewModel::class.java)


        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.layoutManager = layoutManager


        val position = arguments?.getInt(ARG_POSITION)
        val username = arguments?.getString(ARG_USERNAME)


        if (position == 1) {

            detailUserViewModel.getFollowers(username.toString())
            detailUserViewModel.listFollowers.observe(viewLifecycleOwner) { listFollowers ->
                setFollowList(listFollowers)
            }

            detailUserViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }

        } else {
            detailUserViewModel.getFollowings(username.toString())
            detailUserViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
                setFollowList(listFollowing)
            }

            detailUserViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setFollowList(listUser: List<ItemsItem>?) {
        val adapter = UserAdapter().apply {
            this.notifyDataSetChanged()
            submitList(listUser)
        }
        binding.rvUser.adapter = adapter
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }


    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

}