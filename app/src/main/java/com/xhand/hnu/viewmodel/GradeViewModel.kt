package com.xhand.hnu.viewmodel


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class TermCheckBoxes(
    val isChecked: Boolean,
    val term: String
)

class GradeViewModel(settingsViewModel: SettingsViewModel) : ViewModel() {
    private val grade = settingsViewModel.longGradeTerm
    var checkboxes = mutableStateListOf(
        TermCheckBoxes(
            isChecked = false,
            term = grade[0]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = grade[1]
        ),
        TermCheckBoxes(
            isChecked = true,
            term = grade[2]
        ),
        TermCheckBoxes(
            isChecked = true,
            term = grade[3]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = grade[4]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = grade[5]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = grade[6]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = grade[7]
        )
    )

    fun convertTermToIndex(term: MutableList<String>): List<String> {
        term.forEachIndexed { index, _ ->
            val gradeClassString = when (index) {
                0 -> "一"
                1 -> "一"
                2 -> "二"
                3 -> "二"
                4 -> "三"
                5 -> "三"
                6 -> "四"
                7 -> "四"
                else -> ""
            }
            val formOrLater = when ((term.size - index) % 2) {
                0 -> "下"
                else -> "上"
            }
            term[index] = "大${gradeClassString}${formOrLater}"
        }
        return term
    }
}