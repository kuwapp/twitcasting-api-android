package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

data class DeleteCommentJson(@SerializedName("comment_id") val deleteCommentId: String)