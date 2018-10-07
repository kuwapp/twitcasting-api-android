package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.api.json.ResponseErrorJson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal fun createTwitCastingApiClient(clientId: String, clientSecret: String): TwitCastingApiClient {
    val authHeader = TwitCastingAuthHeader(clientId, clientSecret, object : AccessTokenProvider {
        override fun accessToken(): String? = null
    })
    val okHttpClient = createOkHttpClient(authHeader)
    val retrofit = createRetrofit(okHttpClient)
    val bodyConverter = retrofit.responseBodyConverter<ResponseErrorJson>(ResponseErrorJson::class.java, emptyArray())
    val errorConverter = ApiErrorConverter(bodyConverter)
    return TwitCastingApiClientImpl(retrofit.create(TwitCastingService::class.java), errorConverter)
}

private fun createRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl("https://apiv2.twitcasting.tv")
            .build()
}

private fun createOkHttpClient(authHeader: TwitCastingAuthHeader): OkHttpClient {
    return OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request().newBuilder()
                        .headers(authHeader.headers())
                        .build()
                it.proceed(request)
            }
            .build()
}