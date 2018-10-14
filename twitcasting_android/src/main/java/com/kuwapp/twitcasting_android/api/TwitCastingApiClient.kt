package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.core.exception.ApiError
import com.kuwapp.twitcasting_android.core.exception.TwitCastingApiException
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

    fun getSupportingStatus(userId: String, targetUserId: String): Single<GetSupportingStatusJson>

    fun supportUser(targetUserIds: List<String>): Single<SupportUserJson>

    fun unsupportUser(targetUserIds: List<String>): Single<UnsupportUserJson>

    fun getSupportingUsers(userId: String, offset: Int = 0, limit: Int = 20): Single<GetSupportingUsersJson>

    fun getSupportedUsers(userId: String, offset: Int = 0, limit: Int = 20, sortType: SortType = SortType.Ranking): Single<GetSupportedUsersJson>

    fun getCategories(lang: Lang): Single<GetCategoriesJson>

    fun searchUsers(words: String, limit: Int = 50, lang: Lang = Lang.Ja): Single<SearchUsersJson>

    fun searchLiveMovies(limit: Int = 100, type: LiveSearchType, context: String? = null, lang: Lang = Lang.Ja): Single<SearchLiveMoviesJson>

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

    enum class SortType(internal val value: String) {
        New("new"),
        Ranking("ranking")
    }

    enum class Lang(internal val value: String) {
        Ja("ja"),
        En("en")
    }

    enum class LiveSearchType(internal val value: String) {
        Tag("tag"),
        Word("word"),
        Category("category"),
        New("new"),
        Recommend("recommend")
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
        return service.submitComment(movieId, comment, snsPostType.value).mapApiError()
    }

    override fun deleteComment(movieId: String, commentId: String): Single<DeleteCommentJson> {
        return service.deleteComment(movieId, commentId).mapApiError()
    }

    override fun getSupportingStatus(userId: String, targetUserId: String): Single<GetSupportingStatusJson> {
        return service.getSupportingStatus(userId, targetUserId).mapApiError()
    }

    override fun supportUser(targetUserIds: List<String>): Single<SupportUserJson> {
        return service.supportUser(targetUserIds).mapApiError()
    }

    override fun unsupportUser(targetUserIds: List<String>): Single<UnsupportUserJson> {
        return service.unsupportUser(targetUserIds).mapApiError()
    }

    override fun getSupportingUsers(userId: String, offset: Int, limit: Int): Single<GetSupportingUsersJson> {
        return service.getSupportingUsers(userId, offset, limit).mapApiError()
    }

    override fun getSupportedUsers(userId: String, offset: Int, limit: Int, sortType: TwitCastingApiClient.SortType): Single<GetSupportedUsersJson> {
        return service.getSupportedUsers(userId, offset, limit, sortType.value).mapApiError()
    }

    override fun getCategories(lang: TwitCastingApiClient.Lang): Single<GetCategoriesJson> {
        return service.getCategories(lang.value).mapApiError()
    }

    override fun searchUsers(words: String, limit: Int, lang: TwitCastingApiClient.Lang): Single<SearchUsersJson> {
        return service.searchUsers(words, limit, lang.value).mapApiError()
    }

    override fun searchLiveMovies(limit: Int, type: TwitCastingApiClient.LiveSearchType, context: String?, lang: TwitCastingApiClient.Lang): Single<SearchLiveMoviesJson> {
        return service.searchLiveMovies(limit, type.value, context, lang.value)
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

    @GET("/users/{user_id}/supporting_status")
    fun getSupportingStatus(@Path("user_id") userId: String, @Query("target_user_id") targetUserId: String): Single<GetSupportingStatusJson>

    @PUT("/support")
    fun supportUser(@Field("target_user_ids[]") targetUserIds: List<String>): Single<SupportUserJson>

    @PUT("/unsupport")
    fun unsupportUser(@Field("target_user_ids[]") targetUserIds: List<String>): Single<UnsupportUserJson>

    @GET("/users/{user_id}/supporting")
    fun getSupportingUsers(@Path("user_id") userId: String, @Query("offset") offset: Int, @Query("limit") limit: Int): Single<GetSupportingUsersJson>

    @GET("/users/{user_id}/supporters")
    fun getSupportedUsers(@Path("user_id") userId: String, @Query("offset") offset: Int, @Query("limit") limit: Int, @Query("sort") sort: String): Single<GetSupportedUsersJson>

    @GET("/categories")
    fun getCategories(@Query("lang") lang: String): Single<GetCategoriesJson>

    @GET("/search/users")
    fun searchUsers(@Query("words") words: String, @Query("limit") limit: Int, @Query("lang") lang: String): Single<SearchUsersJson>

    @GET("/search/lives")
    fun searchLiveMovies(@Query("limit") limit: Int, @Query("type") type: String, @Query("context") context: String?, @Query("lang") lang: String): Single<SearchLiveMoviesJson>

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