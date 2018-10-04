package com.kuwapp.twitcasting_android

import android.net.Uri
import android.os.Build
import com.kuwapp.twitcasting_android.authorize.TwitcastingUri
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.N_MR1])
class TwitcastingUriTest {

    companion object {

        private const val CALLBACK_URL = "sample://twitcasting.st"
        private const val ACCESS_TOKEN = "AccessToken"
        private val VALID_URI = Uri.parse("$CALLBACK_URL#access_token=$ACCESS_TOKEN&token_type=bearer&expires_in=15552000")
        private val INVALID_URI = Uri.parse("$CALLBACK_URL#result=denied")

    }


    @Test
    fun isValid_givenValidUri_returnTrue() {
        val uriHandler = TwitcastingUri(VALID_URI)
        assertThat(uriHandler.isValid()).isTrue()
    }

    @Test
    fun isValid_givenInvalidUri_returnFalse() {
        val uriHandler = TwitcastingUri(INVALID_URI)
        assertThat(uriHandler.isValid()).isFalse()
    }

    @Test
    fun accessToken_givenValidUri_returnAccessToken() {
        val uriHandler = TwitcastingUri(VALID_URI)
        assertThat(uriHandler.accessToken()).isEqualTo(ACCESS_TOKEN)
    }

    @Test
    fun accessToken_givenInvalidUri_returnNull() {
        val uriHandler = TwitcastingUri(INVALID_URI)
        assertThat(uriHandler.accessToken()).isNull()
    }


}