package com.rizfan.githubuser.data.response

import com.google.gson.annotations.SerializedName

data class RepoResponseItem(

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("html_url")
	val url: String? = null,

	@field:SerializedName("visibility")
	val visibility: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

)
