package com.xhand.hnu.viewmodel


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class TermCheckBoxes(
    val isChecked: Boolean,
    val term: String,
    val termNum: String
)

class GradeViewModel() : ViewModel() {
    private val grade = SettingsViewModel().gradeTerm
    var checkboxes = mutableStateListOf(
        TermCheckBoxes(
            isChecked = false,
            term = "大一 秋季学期",
            termNum = grade[0]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = "大一 春季学期",
            termNum = grade[1]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = "大二 秋季学期",
            termNum = grade[2]
        ),
        TermCheckBoxes(
            isChecked = true,
            term = "大二 春季学期",
            termNum = grade[3]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = "大三 秋季学期",
            termNum = grade[4]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = "大三 春季学期",
            termNum = grade[5]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = "大四 秋季学期",
            termNum = grade[6]
        ),
        TermCheckBoxes(
            isChecked = false,
            term = "大四 春季学期",
            termNum = grade[7]
        )
    )

}