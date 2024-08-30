package com.xhand.hnu.repository

import com.xhand.hnu.model.entity.UserInfoEntity

data class Term(
    val currentLongTerm: String,
    val nextLongTerm: String,
    val currentTerm: String,
    val longGradeTerm: MutableList<String>,
    val gradeTerm: MutableList<String>,
    val lastLongTerm: String
)

object Repository {
    private var token: UserInfoEntity? = null

    fun saveToken(newToken: UserInfoEntity) {
        token = newToken
    }

    fun getToken(): UserInfoEntity? {
        return token
    }

    private var term: Term = Term("", "", "", mutableListOf(), mutableListOf(), "")

    fun saveCurrentTerm(newTerm: Term) {
        term = newTerm
    }

    fun getCurrentTerm(): Term {
        return term
    }
}

