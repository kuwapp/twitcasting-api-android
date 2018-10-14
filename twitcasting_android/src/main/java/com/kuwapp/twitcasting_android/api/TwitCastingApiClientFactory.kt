package com.kuwapp.twitcasting_android.api

import retrofit2.Retrofit

internal fun createCategoryApiClient(retrofit: Retrofit, apiErrorConverter: ApiErrorConverter): TwitCastingCategoryApiClient {
    return TwitCastingCategoryApiClientImpl(
            service = retrofit.create(TwitCastingCategoryService::class.java),
            apiErrorConverter = apiErrorConverter
    )
}

internal fun createUserApiClient(retrofit: Retrofit, apiErrorConverter: ApiErrorConverter): TwitCastingUserApiClient {
    return TwitCastingUserApiClientImpl(
            service = retrofit.create(TwitCastingUserService::class.java),
            apiErrorConverter = apiErrorConverter
    )
}

internal fun createCommentApiClient(retrofit: Retrofit, apiErrorConverter: ApiErrorConverter): TwitCastingCommentApiClient {
    return TwitCastingCommentApiClientImpl(
            service = retrofit.create(TwitCastingCommentService::class.java),
            apiErrorConverter = apiErrorConverter
    )
}

internal fun createMovieApiClient(retrofit: Retrofit, apiErrorConverter: ApiErrorConverter): TwitCastingMovieApiClient {
    return TwitCastingMovieApiClientImpl(
            service = retrofit.create(TwitCastingMovieService::class.java),
            apiErrorConverter = apiErrorConverter
    )
}