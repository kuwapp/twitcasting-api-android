package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class GetMoviesJson(@SerializedName("total_count") val totalCount: Int,
                         @SerializedName("movies") val movies: List<MovieJson>)