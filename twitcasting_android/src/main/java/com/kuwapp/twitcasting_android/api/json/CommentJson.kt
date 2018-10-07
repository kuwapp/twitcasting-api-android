package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

data class CommentJson(@SerializedName("id") val id: String,
                       @SerializedName("message") val message: String,
                       @SerializedName("from_user") val user: UserJson,
                       @SerializedName("created") val createdAt: Int)