package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.api.json.GetUserInfoJson
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface TwitCastingApiClient {

    fun getUserInfo(userId: String): Single<GetUserInfoJson>

}

internal class TwitCastingApiClientImpl(retrofit: Retrofit)
    : TwitCastingApiClient {

    private val service = retrofit.create(TwitCastingService::class.java)

    override fun getUserInfo(userId: String): Single<GetUserInfoJson> {
        return service.getUserInfo(userId)
    }

    internal interface TwitCastingService {

        @GET("/users/{user_id}")
        fun getUserInfo(@Path("user_id") userId: String): Single<GetUserInfoJson>

    }

}