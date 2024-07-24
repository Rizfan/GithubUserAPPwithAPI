package com.rizfan.githubuser.ui.detailuser

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
import com.rizfan.githubuser.data.response.RepoResponseItem
import com.rizfan.githubuser.databinding.FragmentFollowersBinding
import com.rizfan.githubuser.ui.main.UserAdapter

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailUserViewModel::class.java)

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

        } else if(position == 2){
            detailUserViewModel.getFollowings(username.toString())
            detailUserViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
                setFollowList(listFollowing)
            }
            detailUserViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }else{
            detailUserViewModel.getRepo(username.toString())
            detailUserViewModel.listRepo.observe(viewLifecycleOwner) { listRepo ->
                setRepoList(listRepo)
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

    private fun setRepoList(listRepo: List<RepoResponseItem>?) {
        val adapter = RepoAdapter()
        adapter.submitList(listRepo)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_followers, container, false)
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

}