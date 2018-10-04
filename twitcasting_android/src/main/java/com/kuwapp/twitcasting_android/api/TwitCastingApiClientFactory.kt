package com.kuwapp.twitcasting_android.api

internal fun createTwitCastingApiClient(clientId: String, clientSecret: String): TwitCastingApiClient {
    return TwitCastingApiClientImpl(TwitCastingAuthHeader(clientId, clientSecret, object : AccessTokenProvider {
        override fun accessToken(): String? = null
    }))
}