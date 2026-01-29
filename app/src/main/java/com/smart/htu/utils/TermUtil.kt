package com.smart.htu.utils

import com.smart.htu.api.module.SingleTerm
import java.time.LocalDate

object TermUtil {

    fun generateTermList(grade: Int): List<SingleTerm> {
        val year = LocalDate.now().year.toString().take(2)
        return listOf(
            SingleTerm("${year}${grade}01", "${year}${grade}-${year}${grade + 1}-1"),
            SingleTerm("${year}${grade}02", "${year}${grade}-${year}${grade + 1}-2"),
            SingleTerm("${year}${grade + 1}01", "${year}${grade + 1}-${year}${grade + 2}-1"),
            SingleTerm("${year}${grade + 1}02", "${year}${grade + 1}-${year}${grade + 2}-2"),
            SingleTerm("${year}${grade + 2}01", "${year}${grade + 2}-${year}${grade + 3}-1"),
            SingleTerm("${year}${grade + 2}02", "${year}${grade + 2}-${year}${grade + 3}-2"),
            SingleTerm("${year}${grade + 3}01", "${year}${grade + 3}-${year}${grade + 4}-1"),
            SingleTerm("${year}${grade + 3}02", "${year}${grade + 3}-${year}${grade + 4}-2")
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
                TermType.CODE -> "${year.takeIf { it == 1 } ?: (year - 1)}01"
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