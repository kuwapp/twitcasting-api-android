package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

data class SearchLiveMoviesJson(@SerializedName("movies") val movies: List<MovieJson>)