package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.api.json.GetMovieInfoJson
import com.kuwapp.twitcasting_android.api.json.GetMoviesJson
import com.kuwapp.twitcasting_android.api.json.SearchLiveMoviesJson
import com.kuwapp.twitcasting_android.api.model.Movie
import com.kuwapp.twitcasting_android.api.model.MovieInfo
import com.kuwapp.twitcasting_android.api.model.toMovie
import com.kuwapp.twitcasting_android.api.model.toMovieInfo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TwitCastingMovieApiClient {

    fun getMovie(movieId: String): Single<MovieInfo>

    fun getMovies(userId: String, offset: Int = 0, limit: Int = 50, sliceId: Int? = null): Single<Pair<Int, List<Movie>>>

    fun getRecommendMovies(limit: Int = 50): Single<List<MovieInfo>>

    fun getNewMovies(limit: Int = 50): Single<List<MovieInfo>>

    fun searchLiveMoviesByWord(word: String, limit: Int = 50): Single<List<MovieInfo>>

    fun searchLiveMoviesByTag(tag: String, limit: Int = 50): Single<List<MovieInfo>>

    fun searchLiveMoviesByCategory(subCategoryId: String, limit: Int = 50): Single<List<MovieInfo>>

}

internal class TwitCastingMovieApiClientImpl(private val service: TwitCastingMovieService) : TwitCastingMovieApiClient {

    override fun getMovie(movieId: String): Single<MovieInfo> {
        return service.getMovieInfo(
                movieId = movieId
        ).map { it.toMovieInfo() }
    }


    override fun getMovies(userId: String, offset: Int, limit: Int, sliceId: Int?): Single<Pair<Int, List<Movie>>> {
        return service.getMovies(
                userId = userId,
                offset = offset,
                limit = limit,
                sliceId = sliceId
        ).map { response ->
            val totalCount = response.totalCount
            val movies = response.movies.map { it.toMovie() }
            Pair(totalCount, movies)
        }
    }

    override fun getRecommendMovies(limit: Int): Single<List<MovieInfo>> {
        return service.searchLiveMovies(
                limit = limit,
                type = "recommend",
                lang = "ja"
        ).map { response -> response.movies.map { it.toMovieInfo() } }
    }

    override fun getNewMovies(limit: Int): Single<List<MovieInfo>> {
        return service.searchLiveMovies(
                limit = limit,
                type = "new",
                lang = "ja"
        ).map { response -> response.movies.map { it.toMovieInfo() } }
    }

    override fun searchLiveMoviesByWord(word: String, limit: Int): Single<List<MovieInfo>> {
        return service.searchLiveMovies(
                limit = limit,
                type = "word",
                context = word,
                lang = "ja"
        ).map { response -> response.movies.map { it.toMovieInfo() } }
    }

    override fun searchLiveMoviesByTag(tag: String, limit: Int): Single<List<MovieInfo>> {
        return service.searchLiveMovies(
                limit = limit,
                type = "tag",
                context = tag,
                lang = "ja"
        ).map { response -> response.movies.map { it.toMovieInfo() } }
    }

    override fun searchLiveMoviesByCategory(subCategoryId: String, limit: Int): Single<List<MovieInfo>> {
        return service.searchLiveMovies(
                limit = limit,
                type = "category",
                context = subCategoryId,
                lang = "ja"
        ).map { response -> response.movies.map { it.toMovieInfo() } }
    }

}

internal interface TwitCastingMovieService {

    @GET("/movies/{movie_id}")
    fun getMovieInfo(
            @Path("movie_id") movieId: String
    ): Single<GetMovieInfoJson>

    @GET("/users/{user_id}/movies")
    fun getMovies(
            @Path("user_id") userId: String,
            @Query("offset") offset: Int,
            @Query("limit") limit: Int,
            @Query("slice_id") sliceId: Int?
    ): Single<GetMoviesJson>

    @GET("/search/lives")
    fun searchLiveMovies(
            @Query("limit") limit: Int,
            @Query("type") type: String,
            @Query("context") context: String? = null,
            @Query("lang") lang: String
    ): Single<SearchLiveMoviesJson>

}