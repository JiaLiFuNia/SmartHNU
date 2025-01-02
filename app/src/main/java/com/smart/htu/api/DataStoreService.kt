package com.smart.htu.api

import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.application.librarySearch.LibraryBookListEntity
import com.smart.htu.screens.application.librarySearch.RentBookEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.Cookie


interface DataStoreService {

    suspend fun changeDynamicTheme(enabled: Boolean)
    suspend fun changeDarkTheme(isDarkTheme: Int)
    suspend fun saveSmallCard(cardList: List<SmallCardContent>)
    suspend fun changPersonalMessage(message: String)
    suspend fun changeUsername(name: String)
    suspend fun changeLoginState(state: Int)
    suspend fun changeCookies(cookies: List<Cookie>)
    suspend fun changeRentBookList(list: List<RentBookEntity>)

    fun observeDynamicTheme(): Flow<Boolean>
    fun observeDarkTheme(): Flow<Int>
    fun observeSmallCard(): Flow<List<SmallCardContent>>
    fun observePersonalMessage(): Flow<String>
    fun observeUsername(): Flow<String>
    fun observeLoginState(): Flow<Int>
    fun observeCookies(): Flow<List<Cookie>>
    fun observeRentBookList(): Flow<List<RentBookEntity>>

}