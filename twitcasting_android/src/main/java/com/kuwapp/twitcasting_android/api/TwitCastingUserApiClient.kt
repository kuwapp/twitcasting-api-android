package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.api.json.*
import com.kuwapp.twitcasting_android.api.model.User
import com.kuwapp.twitcasting_android.api.model.UserInfo
import com.kuwapp.twitcasting_android.api.model.toUser
import com.kuwapp.twitcasting_android.api.model.toUserInfo
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface TwitCastingUserApiClient {

    fun getUserInfo(userId: String): Single<UserInfo>

    fun searchUsers(words: String, limit: Int = 50): Single<List<User>>

    fun getSupportingStatus(userId: String, targetUserId: String): Single<Boolean>

    fun supportUser(targetUserIds: List<String>): Completable

    fun unsupportUser(targetUserIds: List<String>): Completable

    fun getSupportingUsers(userId: String, offset: Int = 0, limit: Int = 20): Single<List<UserInfo>>

    fun getSupportedUsers(userId: String, offset: Int = 0, limit: Int = 20, sortType: SortType = SortType.Ranking): Single<List<UserInfo>>

    enum class SortType(internal val value: String) {
        New("new"),
        Ranking("ranking")
    }

}

internal class TwitCastingUserApiClientImpl(private val service: TwitCastingUserService,
                                            private val apiErrorConverter: ApiErrorConverter) : TwitCastingUserApiClient {

    override fun getUserInfo(userId: String): Single<UserInfo> {
        return service.getUserInfo(userId)
                .map { it.toUserInfo() }
                .mapApiError(apiErrorConverter)
    }

    override fun searchUsers(words: String, limit: Int): Single<List<User>> {
        return service.searchUsers(
                words = words,
                limit = limit,
                lang = "ja"
        ).map { response ->
            response.users.map { it.toUser() }
        }.mapApiError(apiErrorConverter)
    }

    override fun getSupportingStatus(userId: String, targetUserId: String): Single<Boolean> {
        return service.getSupportingStatus(userId, targetUserId)
                .map { it.isSupporting }
                .mapApiError(apiErrorConverter)
    }

    override fun supportUser(targetUserIds: List<String>): Completable {
        return service.supportUser(targetUserIds)
                .mapApiError(apiErrorConverter)
                .ignoreElement()
    }

    override fun unsupportUser(targetUserIds: List<String>): Completable {
        return service.unsupportUser(targetUserIds)
                .mapApiError(apiErrorConverter)
                .ignoreElement()
    }

    override fun getSupportingUsers(userId: String, offset: Int, limit: Int): Single<List<UserInfo>> {
        return service.getSupportingUsers(
                userId = userId,
                offset = offset,
                limit = limit
        ).map { response ->
            response.supportingUsers.map { it.toUserInfo() }
        }.mapApiError(apiErrorConverter)
    }

    override fun getSupportedUsers(userId: String, offset: Int, limit: Int, sortType: TwitCastingUserApiClient.SortType): Single<List<UserInfo>> {
        return service.getSupportedUsers(
                userId = userId,
                offset = offset,
                limit = limit,
                sort = sortType.value
        ).map { response ->
            response.supportedUsers.map { it.toUserInfo() }
        }.mapApiError(apiErrorConverter)
    }
}

internal interface TwitCastingUserService {

    @GET("/users/{user_id}")
    fun getUserInfo(
            @Path("user_id") userId: String
    ): Single<GetUserInfoJson>

    @GET("/search/users")
    fun searchUsers(
            @Query("words") words: String,
            @Query("limit") limit: Int,
            @Query("lang") lang: String
    ): Single<SearchUsersJson>

    @GET("/users/{user_id}/supporting_status")
    fun getSupportingStatus(
            @Path("user_id") userId: String,
            @Query("target_user_id") targetUserId: String
    ): Single<GetSupportingStatusJson>

    @PUT("/support")
    fun supportUser(
            @Field("target_user_ids[]") targetUserIds: List<String>
    ): Single<SupportUserJson>

    @PUT("/unsupport")
    fun unsupportUser(
            @Field("target_user_ids[]") targetUserIds: List<String>
    ): Single<UnsupportUserJson>

    @GET("/users/{user_id}/supporting")
    fun getSupportingUsers(
            @Path("user_id") userId: String,
            @Query("offset") offset: Int,
            @Query("limit") limit: Int
    ): Single<GetSupportingUsersJson>

    @GET("/users/{user_id}/supporters")
    fun getSupportedUsers(
            @Path("user_id") userId: String,
            @Query("offset") offset: Int,
            @Query("limit") limit: Int,
            @Query("sort") sort: String
    ): Single<GetSupportedUsersJson>

}