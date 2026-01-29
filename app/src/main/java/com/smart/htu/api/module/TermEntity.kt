package com.smart.htu.api.module

import com.google.gson.annotations.SerializedName

data class GlobalTerm(
    @SerializedName("xnxqdm") val termCode: String? = null,
)

data class TermCalendarEntity(
    val msg: String,
    val code: Int,
    val calendar: TermCalendarData,
) {
    data class TermCalendarData(
        val months: List<Month>,
        val termCode: String
    )

    data class Month(
        val month: String,
        val weeks: List<Week>
    )

    data class Week(
        val days: List<Day>,
        @SerializedName("firstDay") val firstDayOfWeek: Int,
        @SerializedName("week") val weekIndex: Int
    )

    data class Day(
        @SerializedName("rq") val date: Int,
        @SerializedName("rqmc") val dateString: String,
        @SerializedName("xqxh") val dayOfWeek: Int
    )

}


data class SingleTerm(
    @SerializedName("xnxqdm") val termCode: String,
    @SerializedName("xnxqmc") val termString: String,
)