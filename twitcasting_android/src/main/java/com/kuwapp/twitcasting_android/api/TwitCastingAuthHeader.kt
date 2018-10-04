package com.kuwapp.twitcasting_android.api

import android.util.Base64
import com.kuwapp.twitcasting_android.BuildConfig
import okhttp3.Headers

internal class TwitCastingAuthHeader(private val clientId: String,
                                     private val clientSecret: String,
                                     private val accessTokenProvider: AccessTokenProvider) {

    fun headers(): Headers {
        val authorizationValue = accessTokenProvider.accessToken()
                ?.let { accessTokenValue(it) } ?: clientIdSecretValue()
        val params = mapOf(
                Pair("X-Api-Version", BuildConfig.API_VERSION),
                Pair("Authorization", authorizationValue)
        )
        return Headers.of(params)
    }

    private fun accessTokenValue(accessToken: String): String {
        return "Bearer $accessToken"
    }

    private fun clientIdSecretValue(): String {
        val clientIdAndSecret = "$clientId:$clientSecret"
        val base64ClientIdSecret = Base64.encodeToString(clientIdAndSecret.toByteArray(), Base64.NO_WRAP)
        return "Basic $base64ClientIdSecret"
    }

}

internal interface AccessTokenProvider {

    fun accessToken(): String?

}