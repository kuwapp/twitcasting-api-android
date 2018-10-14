package com.kuwapp.twitcasting_android.api.model

import com.kuwapp.twitcasting_android.api.json.CommentJson

data class Comment(val id: String,
                   val message: String,
                   val user: User,
                   val createdAt: Int)

internal fun CommentJson.toComment(): Comment {
    return Comment(
            id = id,
            message = message,
            user = user.toUser(),
            createdAt = createdAt
    )
}