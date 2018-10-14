package com.kuwapp.twitcasting_android.api

import com.kuwapp.twitcasting_android.api.json.GetCategoriesJson
import com.kuwapp.twitcasting_android.api.model.Category
import com.kuwapp.twitcasting_android.api.model.toCategory
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitCastingCategoryApiClient {

    fun getCategories(): Single<List<Category>>

}

internal class TwitCastingCategoryApiClientImpl(private val service: TwitCastingCategoryService,
                                                private val apiErrorConverter: ApiErrorConverter) : TwitCastingCategoryApiClient {

    override fun getCategories(): Single<List<Category>> {
        return service.getCategories(
                lang = "ja"
        ).map { response ->
            response.categories.map { it.toCategory() }
        }.mapApiError(apiErrorConverter)
    }
}

internal interface TwitCastingCategoryService {

    @GET("/categories")
    fun getCategories(@Query("lang") lang: String): Single<GetCategoriesJson>

}