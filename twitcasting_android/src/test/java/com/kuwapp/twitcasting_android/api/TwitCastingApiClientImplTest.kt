package com.kuwapp.twitcasting_android.api

import android.os.Build
import com.kuwapp.twitcasting_android.api.json.ResponseErrorJson
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
internal class TwitCastingApiClientImplTest {

    val mockWebServer = MockWebServer()
    lateinit var apiClient: TwitCastingApiClientImpl

    @Before
    fun setUp() {
        val dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                println(request!!.path)
                return when {
                    request == null -> MockResponse().setResponseCode(400)
                    request.path.matches(Regex("/users/[a-zA-Z0-9_]+")) -> MockResponse().setResponseCode(200).setBodyFromFileName("get_user_info.json")
                    request.path.matches(Regex("/users/[a-zA-Z0-9_]+/live/thumbnail(\\?.*)?")) -> MockResponse().setResponseCode(200).setBodyFromFileName("live_thumbnail.png")
                    else -> MockResponse().setResponseCode(400)
                }
            }
        }
        mockWebServer.setDispatcher(dispatcher)
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
                .baseUrl(mockWebServer.url(""))
                .client(OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(TwitCastingService::class.java)
        val bodyConverter = retrofit.responseBodyConverter<ResponseErrorJson>(ResponseErrorJson::class.java, emptyArray())
        val errorConverter = ApiErrorConverter(bodyConverter)
        apiClient = TwitCastingApiClientImpl(service, errorConverter)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getUserInfo_givenUserId_response200() {
        apiClient.getUserInfo("kuwapp_dev")
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()
    }

    @Test
    fun getUserInfo_givenEmptyUserId_response400() {
        val httpException = apiClient.getUserInfo("")
                .test()
                .await()
                .assertError(HttpException::class.java)
                .errors()[0] as HttpException
        assertThat(httpException.response().code()).isEqualTo(400)
    }

    @Test
    fun getLiveThumbnailImage_response200() {
        val response = apiClient.getLiveThumbnailImage("kuwapp_dev")
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()
                .values()[0]
        val inputStream = javaClass.classLoader.getResourceAsStream("live_thumbnail.png")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { buffer -> stringBuilder.append(buffer) }
        val expected = stringBuilder.toString().toByteArray()
        assertThat(response.byteArray).isEqualTo(expected)
    }


}

private fun MockResponse.setBodyFromFileName(name: String): MockResponse {
    val inputStream = javaClass.classLoader.getResourceAsStream(name)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    bufferedReader.forEachLine { buffer -> stringBuilder.append(buffer) }
    val body = stringBuilder.toString()
    this.setBody(body)
    return this
}