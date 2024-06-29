package com.xhand.hnu.viewmodel


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class TermCheckBoxes(
    val isChecked: Boolean,
    val term: String
)

class GradeViewModel : ViewModel() {
    private val grade = SettingsViewModel().longGradeTerm
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

}