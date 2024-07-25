package com.xhand.hnu.model.entity

data class Update(
    val version: String,
    val message: String,
    val confirm: String,
    val type: String
)
