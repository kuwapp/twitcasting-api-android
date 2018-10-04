package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

data class UserJson(@SerializedName("id") val id: String,
                    @SerializedName("screen_id") val screenId: String,
                    @SerializedName("name") val name: String,
                    @SerializedName("thumbnail") val thumbnailUrl: String,
                    @SerializedName("profile") val profileMessage: String,
                    @SerializedName("level") val level: Int,
                    @SerializedName("last_movie_id") val lastMovieId: String,
                    @SerializedName("is_live") val isLive: Boolean)