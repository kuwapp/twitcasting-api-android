package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class SubmitCommentJson(@SerializedName("movie_id") val movieId: String,
                             @SerializedName("all_count") val totalCount: Int,
                             @SerializedName("comment") val comment: CommentJson)