package com.interview.diagnal.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MovieResponse(

	@field:SerializedName("page")
	val page: Page? = null
) : Parcelable

@Parcelize
data class ContentItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("poster-image")
	val posterImage: String? = null
) : Parcelable

@Parcelize
data class ContentItems(

	@field:SerializedName("content")
	val content: List<ContentItem> = ArrayList<ContentItem>()
) : Parcelable

@Parcelize
data class Page(

	@field:SerializedName("page-num")
	val pageNum: String = "",

	@field:SerializedName("page-size")
	val pageSize: String = "",

	@field:SerializedName("content-items")
	val contentItems: ContentItems = ContentItems(),

	@field:SerializedName("total-content-items")
	val totalContentItems: String = "",

	@field:SerializedName("title")
	val title: String = ""
) : Parcelable
