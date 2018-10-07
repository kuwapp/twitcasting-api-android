package com.kuwapp.twitcasting_android

class TwitCastingNotInitializedException : RuntimeException("must call TwitCasting.initialize method.")

data class TwitCastingApiException(val error: ApiError,
                                   val errorMessage: String) : Exception() {


}

enum class ApiError(val code: Int?) {
    Unknown(null),
    InvalidToken(1000),
    ValidationError(1001),
    InvalidWebHookUrl(1002),
    ExecutionCountLimitation(2000),
    ApplicationDisabled(2001),
    Protected(2002),
    Duplicate(2003),
    TooManyComments(2004),
    OutOfScope(2005),
    EmailUnverified(2006),
    BadRequest(400),
    Forbidden(403),
    NotFound(404),
    InternalServerError(500);

    companion object {

        internal fun findBy(code: Int): ApiError {
            return values().find { it.code == code } ?: Unknown
        }

    }


}