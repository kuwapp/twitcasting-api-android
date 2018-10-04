package com.kuwapp.twitcasting_android.api

import android.os.Build
import android.util.Base64
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class TwitCastingAuthHeaderTest {

    companion object {

        private const val CLIENT_ID = "client_id"
        private const val CLIENT_SECRET = "client_secret"
        private val BASE64_CLIENT_ID_SECRET = Base64.encodeToString("$CLIENT_ID:$CLIENT_SECRET".toByteArray(), Base64.NO_WRAP)
        private const val ACCESS_TOKEN = "access_token"

    }

    @Test
    fun headers_givenAccessTokenNull_returnAuthorizationValueIsClientIdSecret() {
        val header = TwitCastingAuthHeader(CLIENT_ID, CLIENT_SECRET, object : AccessTokenProvider {
            override fun accessToken(): String? = null
        })
        val headers = header.headers()
        assertThat(headers.size()).isEqualTo(2)
        assertThat(headers.get("Authorization")).isEqualTo("Basic $BASE64_CLIENT_ID_SECRET")
    }

    @Test
    fun headers_givenAccessTokenNotNull_returnAuthorizationValueIsAccessToken() {
        val header = TwitCastingAuthHeader(CLIENT_ID, CLIENT_SECRET, object : AccessTokenProvider {
            override fun accessToken(): String? = ACCESS_TOKEN
        })
        val headers = header.headers()
        assertThat(headers.size()).isEqualTo(2)
        assertThat(headers.get("Authorization")).isEqualTo("Bearer $ACCESS_TOKEN")
    }

}