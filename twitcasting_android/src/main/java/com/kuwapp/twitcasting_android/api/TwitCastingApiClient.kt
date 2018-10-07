package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.ApiError
import com.kuwapp.twitcasting_android.TwitCastingApiException
import com.kuwapp.twitcasting_android.api.json.GetUserInfoJson
import com.kuwapp.twitcasting_android.api.json.GetLiveThumbnailImage
import com.kuwapp.twitcasting_android.api.json.ResponseErrorJson
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException

interface TwitCastingApiClient {

    fun getUserInfo(userId: String): Single<GetUserInfoJson>

    fun getLiveThumbnailImage(userId: String, size: Size = Size.Small, position: Position = Position.Latest): Single<GetLiveThumbnailImage>

    enum class Size(internal val value: String) {
        Large("large"),
        Small("small")
    }

    enum class Position(internal val value: String) {
        Beginning("beginning"),
        Latest("latest")
    }

}

internal class TwitCastingApiClientImpl(private val service: TwitCastingService,
                                        private val apiErrorConverter: ApiErrorConverter)
    : TwitCastingApiClient {

    override fun getUserInfo(userId: String): Single<GetUserInfoJson> {
        return service.getUserInfo(userId).mapApiError()
    }

    override fun getLiveThumbnailImage(userId: String, size: TwitCastingApiClient.Size, position: TwitCastingApiClient.Position): Single<GetLiveThumbnailImage> {
        return service.getThumbnailImage(userId, size.value, position.value)
                .map { GetLiveThumbnailImage(it.bytes()) }
                .mapApiError()
    }

    private fun <T> Single<T>.mapApiError(): Single<T> {
        return lift { observer ->
            object : SingleObserver<T> {
                override fun onSuccess(t: T) {
                    observer.onSuccess(t)
                }

                override fun onSubscribe(d: Disposable) {
                    observer.onSubscribe(d)
                }

                override fun onError(e: Throwable) {
                    observer.onError(apiErrorConverter.convert(e))
                }
            }
        }
    }

}

internal interface TwitCastingService {

    @GET("/users/{user_id}")
    fun getUserInfo(@Path("user_id") userId: String): Single<GetUserInfoJson>

    @GET("/users/{user_id}/live/thumbnail")
    fun getThumbnailImage(@Path("user_id") userId: String, @Query("size") size: String, @Query("position") position: String): Single<ResponseBody>

}

internal class ApiErrorConverter(private val errorConverter: Converter<ResponseBody, ResponseErrorJson>) {

    fun convert(throwable: Throwable): Throwable {
        if (throwable !is HttpException) return throwable
        val errorBody = throwable.response().errorBody() ?: return throwable
        try {
            val error = errorConverter.convert(errorBody)?.error ?: return throwable
            return TwitCastingApiException(ApiError.findBy(error.code), error.message)
        } catch (e: IOException) {
            return throwable
        }
    }

}