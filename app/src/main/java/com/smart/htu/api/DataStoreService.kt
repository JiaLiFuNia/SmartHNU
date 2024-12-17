package com.smart.htu.api

import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.login.EditablePersonalMessage
import kotlinx.coroutines.flow.Flow


interface DataStoreService {

    suspend fun changeDynamicTheme(enabled: Boolean)
    suspend fun changeDarkTheme(isDarkTheme: Int)
    suspend fun saveSmallCard(cardList: List<SmallCardContent>)
    suspend fun changPersonalMessage(message: EditablePersonalMessage)
    suspend fun changeLoginState(state: Int)

    fun observeDynamicTheme(): Flow<Boolean>
    fun observeDarkTheme(): Flow<Int>
    fun observeSmallCard(): Flow<List<SmallCardContent>>
    fun observePersonalMessage(): Flow<EditablePersonalMessage>
    fun observeLoginState(): Flow<Int>

}