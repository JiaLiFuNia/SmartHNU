package com.smart.htu.api.module

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class ResultWithStatus<T>(val data: T?, val status: Status) {
    constructor() : this(null, Status.LOADING)

    constructor(data: T?) : this(data, if (data == null) Status.ERROR else Status.SUCCESS)
}
