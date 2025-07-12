package com.smart.htu.utils

import com.smart.htu.api.module.SingleTerm
import java.time.LocalDate

object TermUtil {

    fun generateTermList(grade: Int): List<SingleTerm> {
        return listOf(
            SingleTerm("${grade}01", "${grade}-${grade + 1}-1"),
            SingleTerm("${grade}02", "${grade}-${grade + 1}-2"),
            SingleTerm("${grade + 1}01", "${grade + 1}-${grade + 2}-1"),
            SingleTerm("${grade + 1}02", "${grade + 1}-${grade + 2}-2"),
            SingleTerm("${grade + 2}01", "${grade + 2}-${grade + 3}-1"),
            SingleTerm("${grade + 2}02", "${grade + 2}-${grade + 3}-2"),
            SingleTerm("${grade + 3}01", "${grade + 3}-${grade + 4}-1"),
            SingleTerm("${grade + 3}02", "${grade + 3}-${grade + 4}-2")
        )
    }

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