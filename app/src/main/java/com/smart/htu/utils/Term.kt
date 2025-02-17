package com.smart.htu.utils

import java.time.LocalDate

object Term {

    // 202401
    // 2024-2025-1

    fun getCurrentTerm(length: TermType = TermType.CODE): String {
        val today = LocalDate.now()
        val year = today.year
        val month = today.month.value
        // 如果为9月到次年1月，为第一学期
        return if (month == 1 || month in 9..12) {
            when (length) {
                TermType.CODE -> "${year}01"
                TermType.STRING -> "${year}-${year + 1}-1"
            }
        } else {
            when (length) {
                TermType.CODE -> "${year - 1}02"
                TermType.STRING -> "${year - 1}-${year}-2"
            }
        }
    }

    fun termConverter(term: String): String {
        return when (term.length) {
            6 -> {
                val year = term.substring(0, 4).toInt()
                "${year}-${year + 1}-${term.last()}"
            }

            11 -> {
                val list = term.split('-')
                "${list.first()}0${list.last()}"
            }

            else -> getCurrentTerm()
        }
    }

}

enum class TermType {
    CODE, STRING
}