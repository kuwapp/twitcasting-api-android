package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

data class GetCommentsJson(@SerializedName("movie_id") val movieId: String,
                           @SerializedName("all_count") val totalCount: Int,
                           @SerializedName("comments") val comments: List<CommentJson>)