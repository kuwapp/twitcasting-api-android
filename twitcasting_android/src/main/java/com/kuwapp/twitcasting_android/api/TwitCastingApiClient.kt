package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.ApiError
import com.kuwapp.twitcasting_android.TwitCastingApiException
import com.kuwapp.twitcasting_android.api.json.*
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.http.*
import java.io.IOException

interface TwitCastingApiClient {

    fun getUserInfo(userId: String): Single<GetUserInfoJson>

    fun getLiveThumbnailImage(userId: String, size: Size = Size.Small, position: Position = Position.Latest): Single<GetLiveThumbnailImage>

    fun getMovieInfo(movieId: String): Single<GetMovieInfoJson>

    fun getMovies(userId: String, offset: Int = 0, limit: Int = 50, sliceId: Int? = null): Single<GetMoviesJson>

    fun getComments(movieId: String, offset: Int = 0, limit: Int = 50, sliceId: Int? = null): Single<GetCommentsJson>

    fun submitComment(movieId: String, comment: String, snsPostType: SnsPostType = SnsPostType.None): Single<SubmitCommentJson>

    fun deleteComment(movieId: String, commentId: String): Single<DeleteCommentJson>

    enum class Size(internal val value: String) {
        Large("large"),
        Small("small")
    }

    enum class Position(internal val value: String) {
        Beginning("beginning"),
        Latest("latest")
    }

    enum class SnsPostType(internal val value: String) {
        Reply("reply"),
        Normal("normal"),
        None("none")
    }

}

internal class TwitCastingApiClientImpl(private val service: TwitCastingService,
                                        private val apiErrorConverter: ApiErrorConverter)
    : TwitCastingApiClient {

    override fun getUserInfo(userId: String): Single<GetUserInfoJson> {
        return service.getUserInfo(userId).mapApiError()
    }

    override fun getLiveThumbnailImage(userId: String, size: TwitCastingApiClient.Size, position: TwitCastingApiClient.Position): Single<GetLiveThumbnailImage> {
        return service.getThumbnailImage(userId, size.value, position.value)
                .map { GetLiveThumbnailImage(it.bytes()) }
                .mapApiError()
    }

    override fun getMovieInfo(movieId: String): Single<GetMovieInfoJson> {
        return service.getMovieInfo(movieId).mapApiError()
    }

    override fun getMovies(userId: String, offset: Int, limit: Int, sliceId: Int?): Single<GetMoviesJson> {
        return service.getMovies(userId, offset, limit, sliceId).mapApiError()
    }


    override fun getComments(movieId: String, offset: Int, limit: Int, sliceId: Int?): Single<GetCommentsJson> {
        return service.getComments(movieId, offset, limit, sliceId).mapApiError()
    }


    override fun submitComment(movieId: String, comment: String, snsPostType: TwitCastingApiClient.SnsPostType): Single<SubmitCommentJson> {
        return service.submitComment(movieId, comment, snsPostType.value)
    }

    override fun deleteComment(movieId: String, commentId: String): Single<DeleteCommentJson> {
        return service.deleteComment(movieId, commentId)
    }

    private fun <T> Single<T>.mapApiError(): Single<T> {
        return lift { observer ->
            object : SingleObserver<T> {
                override fun onSuccess(t: T) {
                    observer.onSuccess(t)
                }

                override fun onSubscribe(d: Disposable) {
                    observer.onSubscribe(d)
                }

                override fun onError(e: Throwable) {
                    observer.onError(apiErrorConverter.convert(e))
                }
            }
        }
    }

}

internal interface TwitCastingService {

    @GET("/users/{user_id}")
    fun getUserInfo(@Path("user_id") userId: String): Single<GetUserInfoJson>

    @GET("/users/{user_id}/live/thumbnail")
    fun getThumbnailImage(@Path("user_id") userId: String, @Query("size") size: String, @Query("position") position: String): Single<ResponseBody>

    @GET("/movies/{movie_id}")
    fun getMovieInfo(@Path("movie_id") movieId: String): Single<GetMovieInfoJson>

    @GET("/users/{user_id}/movies")
    fun getMovies(@Path("user_id") userId: String, @Query("offset") offset: Int, @Query("limit") limit: Int, @Query("slice_id") sliceId: Int?): Single<GetMoviesJson>

    @GET("/movies/{movie_id}/comments")
    fun getComments(@Path("movie_id") movieId: String, @Query("offset") offset: Int, @Query("limit") limit: Int, @Query("slice_id") sliceId: Int?): Single<GetCommentsJson>

    @POST("/movies/{movie_id}/comments")
    fun submitComment(@Path("movie_id") movieId: String, @Field("comment") comment: String, @Field("sns") snsType: String): Single<SubmitCommentJson>

    @DELETE("/movies/{movie_id}/comments/{comment_id}")
    fun deleteComment(@Path("movie_id") movieId: String, @Path("comment_id") commentId: String): Single<DeleteCommentJson>


}

internal class ApiErrorConverter(private val errorConverter: Converter<ResponseBody, ResponseErrorJson>) {

    fun convert(throwable: Throwable): Throwable {
        if (throwable !is HttpException) return throwable
        val errorBody = throwable.response().errorBody() ?: return throwable
        try {
            val error = errorConverter.convert(errorBody)?.error ?: return throwable
            return TwitCastingApiException(ApiError.findBy(error.code), error.message)
        } catch (e: IOException) {
            return throwable
        }
    }

}