package com.smart.htu.screens.login

import kotlinx.serialization.Serializable


@Serializable
data class EditablePersonalMessage(
    val customUsername: String,
    val qqNumber: String
)

@Serializable
data class UneditablePersonalMessage(
    val username: String,
    val academic: String,
    val studentId: String,
    val phoneNumber: Long,
    val emailNumber: String
)
