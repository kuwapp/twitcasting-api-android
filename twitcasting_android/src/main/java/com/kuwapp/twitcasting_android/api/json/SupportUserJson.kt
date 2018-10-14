package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class SupportUserJson(@SerializedName("id") val id: String,
                           @SerializedName("screen_id") val screenId: String,
                           @SerializedName("name") val name: String,
                           @SerializedName("image") val thumbnailUrl: String,
                           @SerializedName("profile") val profileMessage: String,
                           @SerializedName("level") val level: Int,
                           @SerializedName("last_movie_id") val lastMovieId: String?,
                           @SerializedName("is_live") val isLive: Boolean,
                           @SerializedName("supporter_count") val supporterCount: Int,
                           @SerializedName("supporting_count") val supportingCount: Int,
                           @SerializedName("point") val point: Int,
                           @SerializedName(":total_point") val totalPoint: Int)