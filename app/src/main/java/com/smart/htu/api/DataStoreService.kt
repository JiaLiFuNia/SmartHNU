package com.smart.htu.api

import com.smart.htu.api.module.LoginCookie
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
    suspend fun saveAirConditionCookieType(type: Int)
    suspend fun saveAirConditionUserCookie(cookie: LoginCookie)
    suspend fun changeBookSearchHistoryList(list: List<String>)
    suspend fun setJWCToken(token: String)
    suspend fun changeLoginJWCState(state: Int)
    suspend fun saveNoticeReadId(id: List<String>)
    suspend fun setTokenValid(valid: Boolean)

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
    fun observeAirConditionCookieType(): Flow<Int>
    fun observeAirConditionUserCookie(): Flow<LoginCookie>
    fun observeBookSearchHistoryList(): Flow<List<String>>
    fun observeJWCToken(): Flow<String>
    fun observeLoginJWCState(): Flow<Int>
    fun observeNoticeReadIdList(): Flow<List<String>>
    fun observeTokenValid(): Flow<Boolean>

}