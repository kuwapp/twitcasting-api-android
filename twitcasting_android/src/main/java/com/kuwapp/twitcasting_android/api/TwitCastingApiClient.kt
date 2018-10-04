package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.ApiError
import com.kuwapp.twitcasting_android.TwitCastingApiException
import com.kuwapp.twitcasting_android.api.json.GetUserInfoJson
import com.kuwapp.twitcasting_android.api.json.ResponseErrorJson
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface TwitCastingApiClient {

    fun getUserInfo(userId: String): Single<GetUserInfoJson>

}

internal class TwitCastingApiClientImpl(retrofit: Retrofit)
    : TwitCastingApiClient {

    private val errorConverter = retrofit.responseBodyConverter<ResponseErrorJson>(ResponseErrorJson::class.java, emptyArray())
    private val service = retrofit.create(TwitCastingService::class.java)

    override fun getUserInfo(userId: String): Single<GetUserInfoJson> {
        return service.getUserInfo(userId).mapApiError()
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
                    val apiError = (e as? HttpException)?.response()?.errorBody()?.let {
                        errorConverter.convert(it)
                    }?.error ?: run {
                        observer.onError(e)
                        return
                    }
                    observer.onError(TwitCastingApiException(ApiError.findBy(apiError.code), apiError.message))
                }
            }
        }
    }

    internal interface TwitCastingService {

        @GET("/users/{user_id}")
        fun getUserInfo(@Path("user_id") userId: String): Single<GetUserInfoJson>

    }

}