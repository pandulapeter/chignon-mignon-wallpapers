package com.chignonMignon.wallpapers.domain

import com.chignonMignon.wallpapers.data.model.Result

internal inline fun <T> resultOf(action: () -> T) = try {
    Result.Success(action())
} catch (exception: Exception) {
    Result.Failure(exception)
}