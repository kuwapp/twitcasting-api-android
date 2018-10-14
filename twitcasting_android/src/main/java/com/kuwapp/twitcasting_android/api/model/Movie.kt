package com.kuwapp.twitcasting_android.api.model

import com.kuwapp.twitcasting_android.api.json.MovieJson

data class Movie(val id: String,
                 val userId: String,
                 val title: String,
                 val subTitle: String?,
                 val lastOwnerComment: String?,
                 val categoryId: String?,
                 val linkUrl: String,
                 val isLive: Boolean,
                 val isRecorded: Boolean,
                 val commentCount: Int,
                 val largeThumbnail: String,
                 val smallThumbnail: String,
                 val country: String,
                 val duration: Int,
                 val createdAt: Int,
                 val isCollabo: Boolean,
                 val isProtected: Boolean,
                 val maxViewerCount: Int,
                 val currentViewerCount: Int,
                 val totalViewerCount: Int,
                 val hlsUrl: String)

internal fun MovieJson.toMovie(): Movie {
    return Movie(
            id = id,
            userId = userId,
            title = title,
            subTitle = subTitle,
            lastOwnerComment = lastOwnerComment,
            categoryId = categoryId,
            linkUrl = linkUrl,
            isLive = isLive,
            isRecorded = isRecorded,
            commentCount = commentCount,
            largeThumbnail = largeThumbnail,
            smallThumbnail = smallThumbnail,
            country = country,
            duration = duration,
            createdAt = createdAt,
            isCollabo = isCollabo,
            isProtected = isProtected,
            maxViewerCount = maxViewerCount,
            currentViewerCount = currentViewerCount,
            totalViewerCount = totalViewerCount,
            hlsUrl = hlsUrl
    )
}