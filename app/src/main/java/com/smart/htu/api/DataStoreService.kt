package com.smart.htu.api

import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.application.librarySearch.LibraryBookListEntity
import com.smart.htu.screens.application.librarySearch.RentBookEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.Cookie


interface DataStoreService {

    suspend fun changeDynamicTheme(enabled: Boolean)
    suspend fun changeDarkTheme(isDarkTheme: Int)
    suspend fun changeBlurState(state: Boolean)
    suspend fun saveSmallCard(cardList: List<SmallCardContent>)
    suspend fun changPersonalMessage(message: String)
    suspend fun changeUsername(name: String)
    suspend fun changeLoginState(state: Int)
    suspend fun saveCookies(cookies: List<Cookie>)
    suspend fun changeRentBookList(list: List<RentBookEntity>)
    suspend fun saveStudentId(id: String)
    suspend fun changeBuildingId(id: String)
    suspend fun changeRoomId(room: String)

    fun observeDynamicTheme(): Flow<Boolean>
    fun observeDarkTheme(): Flow<Int>
    fun observerBlurState(): Flow<Boolean>
    fun observeSmallCard(): Flow<List<SmallCardContent>>
    fun observePersonalMessage(): Flow<String>
    fun observeUsername(): Flow<String>
    fun observeLoginState(): Flow<Int>
    fun observeCookies(): Flow<List<Cookie>>
    fun observeRentBookList(): Flow<List<RentBookEntity>>
    fun observeStudentId(): Flow<String>
    fun observeBuildingId(): Flow<String>
    fun observeRoomId(): Flow<String>

}