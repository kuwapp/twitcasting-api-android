package com.kuwapp.twitcasting_android.api.json

import com.google.gson.annotations.SerializedName

internal data class GetMovieInfoJson(@SerializedName("movie") val movie: MovieJson,
                            @SerializedName("broadcaster") val broadcaster: UserJson,
                            @SerializedName("tags") val tags: List<String>)