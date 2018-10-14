package com.kuwapp.twitcasting_android.api.model

import com.kuwapp.twitcasting_android.api.json.CategoryJson

data class Category(val id: String,
                        val name: String,
                        val subCategories: List<SubCategory>)

internal fun CategoryJson.toCategory(): Category {
    return Category(
            id = id,
            name = name,
            subCategories = subCategories.map { it.toSubCategory() }
    )
}