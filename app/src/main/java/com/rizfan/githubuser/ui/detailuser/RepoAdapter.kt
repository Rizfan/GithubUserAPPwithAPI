package com.rizfan.githubuser.ui.detailuser

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rizfan.githubuser.data.response.RepoResponseItem
import com.rizfan.githubuser.databinding.ItemRowRepoBinding

class RepoAdapter: ListAdapter<RepoResponseItem, RepoAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         val repo = getItem(position)
         holder.bind(repo)
    }

    class MyViewHolder(private val binding: ItemRowRepoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: RepoResponseItem) {
            binding.tvRepoName.text = repo.name
            if (repo.description.isNullOrEmpty()) binding.tvDesc.text = "No Description"
            else binding.tvDesc.text = repo.description
            binding.tvLanguage.text = repo.language
            binding.tvVisibility.text = "(${repo.visibility})"
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.url))
                itemView.context.startActivity(intent)
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RepoResponseItem>() {
            override fun areItemsTheSame(
                oldItem: RepoResponseItem,
                newItem: RepoResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RepoResponseItem,
                newItem: RepoResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}