package com.smart.htu.screens.main.entity

data class SingleCourseEntity(
    val courseName: String,
    val courseCode: String,
    val teacher: String,
    val position: String,
    val time: String,
    val investigationOrExamination: String,
    val courseEnglishName: String? = ""
)
