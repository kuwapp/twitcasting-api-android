package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.api.json.ResponseErrorJson
import com.kuwapp.twitcasting_android.core.TwitCasting
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object TwitCastingApi {

    private val retrofit: Retrofit by lazy {

        fun createRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .baseUrl("https://apiv2.twitcasting.tv")
                    .build()
        }

        fun createOkHttpClient(authHeader: TwitCastingAuthHeader): OkHttpClient {
            return OkHttpClient.Builder()
                    .addInterceptor {
                        val request = it.request().newBuilder()
                                .headers(authHeader.headers())
                                .build()
                        it.proceed(request)
                    }
                    .build()
        }

        val config = TwitCasting.config()
        val authHeader = TwitCastingAuthHeader(config.clientId, config.clientSecret, object : AccessTokenProvider {
            override fun accessToken(): String? = null
        })
        val okHttpClient = createOkHttpClient(authHeader)
        createRetrofit(okHttpClient)
    }

    private val apiErrorConverter: ApiErrorConverter by lazy {
        val bodyConverter = retrofit.responseBodyConverter<ResponseErrorJson>(ResponseErrorJson::class.java, emptyArray())
        ApiErrorConverter(bodyConverter)
    }

    val categoryApiClient by lazy {
        createCategoryApiClient(retrofit, apiErrorConverter)
    }

    val commentApiClient by lazy {
        createCommentApiClient(retrofit, apiErrorConverter)
    }

    val movieApiClient by lazy {
        createMovieApiClient(retrofit, apiErrorConverter)
    }

    val userApiClient by lazy {
        createUserApiClient(retrofit, apiErrorConverter)
    }

}