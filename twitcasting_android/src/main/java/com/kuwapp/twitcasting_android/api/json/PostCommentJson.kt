package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

data class PostCommentJson(@SerializedName("movie_id") val movieId: String,
                           @SerializedName("all_count") val totalCount: Int,
                           @SerializedName("comment") val comment: CommentJson)