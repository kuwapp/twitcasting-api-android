package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.api.json.GetUserInfoJson
import com.kuwapp.twitcasting_android.api.json.SearchUsersJson
import com.kuwapp.twitcasting_android.api.model.User
import com.kuwapp.twitcasting_android.api.model.UserInfo
import com.kuwapp.twitcasting_android.api.model.toUser
import com.kuwapp.twitcasting_android.api.model.toUserInfo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TwitCastingUserApiClient {

    fun getUserInfo(userId: String): Single<UserInfo>

    fun searchUsers(words: String, limit: Int = 50): Single<List<User>>

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

}

internal interface TwitCastingUserService {

    @GET("/users/{user_id}")
    fun getUserInfo(@Path("user_id") userId: String): Single<GetUserInfoJson>

    @GET("/search/users")
    fun searchUsers(@Query("words") words: String, @Query("limit") limit: Int, @Query("lang") lang: String): Single<SearchUsersJson>

}