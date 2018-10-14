package com.kuwapp.twitcasting_android.api.model

import com.kuwapp.twitcasting_android.api.json.GetMovieInfoJson

data class MovieInfo(val movie: Movie,
                     val broadcaster: User,
                     val tags: List<String>)

internal fun GetMovieInfoJson.toMovieInfo(): MovieInfo {
    return MovieInfo(
            movie = movie.toMovie(),
            broadcaster = broadcaster.toUser(),
            tags = tags
    )
}