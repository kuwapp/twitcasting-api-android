package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.api.json.ResponseErrorJson
import com.kuwapp.twitcasting_android.core.exception.ApiError
import com.kuwapp.twitcasting_android.core.exception.TwitCastingApiException
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException

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

internal fun <T> Single<T>.mapApiError(converter: ApiErrorConverter): Single<T> {
    return lift { observer ->
        object : SingleObserver<T> {
            override fun onSuccess(t: T) {
                observer.onSuccess(t)
            }

            override fun onSubscribe(d: Disposable) {
                observer.onSubscribe(d)
            }

            override fun onError(e: Throwable) {
                observer.onError(converter.convert(e))
            }
        }
    }
}