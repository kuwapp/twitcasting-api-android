package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class MovieJson(@SerializedName("id") val id: String,
                     @SerializedName("user_id") val userId: String,
                     @SerializedName("title") val title: String,
                     @SerializedName("subtitle") val subTitle: String?,
                     @SerializedName("last_owner_comment") val lastOwnerComment: String?,
                     @SerializedName("category") val categoryId: String?,
                     @SerializedName("link") val linkUrl: String,
                     @SerializedName("is_live") val isLive: Boolean,
                     @SerializedName("is_recorded") val isRecorded: Boolean,
                     @SerializedName("comment_count") val commentCount: Int,
                     @SerializedName("large_thumbnail") val largeThumbnail: String,
                     @SerializedName("small_thumbnail") val smallThumbnail: String,
                     @SerializedName("country") val country: String,
                     @SerializedName("duration") val duration: Int,
                     @SerializedName("created") val createdAt: Int,
                     @SerializedName("is_collabo") val isCollabo: Boolean,
                     @SerializedName("is_protected") val isProtected: Boolean,
                     @SerializedName("max_view_count") val maxViewerCount: Int,
                     @SerializedName("current_view_count") val currentViewerCount: Int,
                     @SerializedName("total_view_count") val totalViewerCount: Int,
                     @SerializedName("hls_url") val hlsUrl: String)