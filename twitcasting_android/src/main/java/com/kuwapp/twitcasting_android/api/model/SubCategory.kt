package com.kuwapp.twitcasting_android.api.model

import com.kuwapp.twitcasting_android.api.json.SubCategoryJson

data class SubCategory(val id: String,
                       val name: String,
                       val count: Int)

internal fun SubCategoryJson.toSubCategory(): SubCategory {
    return SubCategory(
            id = id,
            name = name,
            count = count
    )
}