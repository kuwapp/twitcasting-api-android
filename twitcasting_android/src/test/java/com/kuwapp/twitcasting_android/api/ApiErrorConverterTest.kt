package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.core.exception.ApiError
import com.kuwapp.twitcasting_android.core.exception.TwitCastingApiException
import com.kuwapp.twitcasting_android.api.json.ErrorJson
import com.kuwapp.twitcasting_android.api.json.ResponseErrorJson
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import okhttp3.ResponseBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response

internal class ApiErrorConverterTest {

    companion object {

        private fun createBodyConvereter_with_returnNull(): Converter<ResponseBody, ResponseErrorJson> {
            return mock {
                on { convert(any()) }.thenReturn(null)
            }
        }

        private fun createBodyConvereter_with_returnNotNull(returnValue: ResponseErrorJson): Converter<ResponseBody, ResponseErrorJson> {
            return mock {
                on { convert(any()) } doReturn returnValue
            }
        }

    }

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
    }

    @Test
    fun convert_givenNotHttpException_returnThrowable() {
        val converter = ApiErrorConverter(createBodyConvereter_with_returnNull())
        val throwable = Throwable()
        val actual = converter.convert(throwable)
        assertThat(actual).isEqualTo(throwable)
    }

    @Test
    fun convert_givenHttpExceptionWithErrorBodyNull_returnHttpException() {
        val converter = ApiErrorConverter(createBodyConvereter_with_returnNull())
        val response = mock<Response<*>> {
            on { errorBody() }.thenReturn(null)
        }
        val httpException = mock<HttpException> {
            on { response() } doReturn response
        }
        val actual = converter.convert(httpException)
        assertThat(actual).isEqualTo(httpException)
    }

    @Test
    fun convert_givenHttpExceptionWithErrorBody_returnHttpException() {
        val converter = ApiErrorConverter(createBodyConvereter_with_returnNull())
        val errorBody = mock<ResponseBody>()
        val response = mock<Response<*>> {
            on { errorBody() } doReturn errorBody
        }
        val httpException = mock<HttpException> {
            on { response() } doReturn response
        }
        val actual = converter.convert(httpException)
        assertThat(actual).isEqualTo(httpException)
    }

    @Test
    fun convert_givenHttpExceptionWithTwitCastingErrorBody_returnTwitCastingApiException() {
        val responseErrorJson = ResponseErrorJson(ErrorJson(code = 1000, message = "Invalid token"))
        val converter = ApiErrorConverter(createBodyConvereter_with_returnNotNull(responseErrorJson))
        val errorBody = mock<ResponseBody>()
        val response = mock<Response<*>> {
            on { errorBody() } doReturn errorBody
        }
        val httpException = mock<HttpException> {
            on { response() } doReturn response
        }
        val actual = converter.convert(httpException)
        val expect = TwitCastingApiException(ApiError.InvalidToken, "Invalid token")
        assertThat(actual).isEqualTo(expect)
    }

}