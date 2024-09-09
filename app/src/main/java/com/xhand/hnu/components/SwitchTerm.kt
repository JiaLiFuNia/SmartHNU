package com.xhand.hnu.components

fun longToShort(xq: String): String {
    return if (xq.length == 6) {
        "${xq.substring(0, 4)}-${xq.substring(0, 4).toInt() + 1}-${xq.last()}"
    } else
        "${xq.substring(0, 4)}0${xq.last()}"
}