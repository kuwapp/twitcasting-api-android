package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.api.json.DeleteCommentJson
import com.kuwapp.twitcasting_android.api.json.GetCommentsJson
import com.kuwapp.twitcasting_android.api.json.SubmitCommentJson
import com.kuwapp.twitcasting_android.api.model.Comment
import com.kuwapp.twitcasting_android.api.model.toComment
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface TwitCastingCommentApiClient {

    fun getComments(movieId: String, offset: Int = 0, limit: Int = 50, sliceId: Int? = null): Single<List<Comment>>

    fun submitComment(movieId: String, comment: String, snsPostType: SnsPostType = SnsPostType.None): Single<Comment>

    fun deleteComment(movieId: String, commentId: String): Completable

    enum class SnsPostType(internal val value: String) {
        Reply("reply"),
        Normal("normal"),
        None("none")
    }

}

internal class TwitCastingCommentApiClientImpl(private val service: TwitCastingCommentService,
                                               private val apiErrorConverter: ApiErrorConverter) : TwitCastingCommentApiClient {

    override fun getComments(movieId: String, offset: Int, limit: Int, sliceId: Int?): Single<List<Comment>> {
        return service.getComments(
                movieId = movieId,
                offset = offset,
                limit = limit,
                sliceId = sliceId
        ).map { response ->
            response.comments.map { it.toComment() }
        }.mapApiError(apiErrorConverter)
    }

    override fun submitComment(movieId: String, comment: String, snsPostType: TwitCastingCommentApiClient.SnsPostType): Single<Comment> {
        return service.submitComment(
                movieId = movieId,
                comment = comment,
                snsType = snsPostType.value
        ).map {
            it.comment.toComment()
        }.mapApiError(apiErrorConverter)
    }

    override fun deleteComment(movieId: String, commentId: String): Completable {
        return service.deleteComment(
                movieId = movieId,
                commentId = commentId
        ).mapApiError(apiErrorConverter).ignoreElement()
    }

}

internal interface TwitCastingCommentService {

    @GET("/movies/{movie_id}/comments")
    fun getComments(
            @Path("movie_id") movieId: String,
            @Query("offset") offset: Int,
            @Query("limit") limit: Int,
            @Query("slice_id") sliceId: Int?
    ): Single<GetCommentsJson>

    @POST("/movies/{movie_id}/comments")
    fun submitComment(
            @Path("movie_id") movieId: String,
            @Field("comment") comment: String,
            @Field("sns") snsType: String
    ): Single<SubmitCommentJson>

    @DELETE("/movies/{movie_id}/comments/{comment_id}")
    fun deleteComment(
            @Path("movie_id") movieId: String,
            @Path("comment_id") commentId: String
    ): Single<DeleteCommentJson>

}