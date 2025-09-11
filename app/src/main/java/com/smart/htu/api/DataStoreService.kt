package com.smart.htu.api

import com.smart.htu.api.module.ACCookie
import com.smart.htu.api.module.AIModelConfigEntity
import com.smart.htu.api.module.LibraryDetailEntity
import com.smart.htu.api.module.NewsMarkEntity
import com.smart.htu.screens.application.ApplicationEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.Cookie


interface DataStoreService {

    suspend fun changeThemeMode(enabled: Int)
    suspend fun changeDarkTheme(isDarkTheme: Int)
    suspend fun changeBlurState(state: Boolean)
    suspend fun setCommonApp(appList: List<ApplicationEntity>)
    suspend fun changeUsername(name: String)
    suspend fun changeLoginState(state: Int)
    suspend fun changeLoginJWCState(state: Int)
    suspend fun saveAuthCookie(cookies: List<Cookie>)
    suspend fun addWaitingBorrowedBookList(waitingBorrowedBookList: List<LibraryDetailEntity>)
    suspend fun saveStudentId(id: String)
    suspend fun changeBuildingId(id: String)
    suspend fun changeRoomId(room: String)
    suspend fun saveAirConditionCookieType(type: Int)
    suspend fun saveAirConditionUserCookie(cookie: ACCookie)
    suspend fun changeBookSearchHistoryList(list: List<String>)
    suspend fun setJWCToken(token: String)
    suspend fun addReadNoticeId(id: List<Int>)
    suspend fun setGlobalTermCode(term: String)
    suspend fun saveMobileCode(mobileCode: String)
    suspend fun setIsWriteCalendarPermissionGranted(enable: Boolean)
    suspend fun changeAIFunctionEnabled(enabled: Boolean)
    suspend fun saveAIModelConfig(config: AIModelConfigEntity)
    suspend fun changeBionicReadingEnabled(enabled: Boolean)
    suspend fun changeLoadImgEnabled(enable: Boolean)
    suspend fun changeNewsHistoryList(newsItem: NewsMarkEntity)
    suspend fun addNewsFavoriteList(newsItem: NewsMarkEntity)
    suspend fun changeNewsFontSize(size: Int)

    fun observeThemeMode(): Flow<Int>
    fun observeDarkTheme(): Flow<Int>
    fun observerBlurState(): Flow<Boolean>
    fun observeCommonAppList(): Flow<List<ApplicationEntity>>
    fun observeUsername(): Flow<String>
    fun observeLoginState(): Flow<Int>
    fun observeAuthCookie(): Flow<List<Cookie>>
    fun observeWaitingBorrowedBookList(): Flow<List<LibraryDetailEntity>>
    fun observeStudentId(): Flow<String>
    fun observeBuildingId(): Flow<String>
    fun observeRoomId(): Flow<String>
    fun observeAirConditionCookieType(): Flow<Int>
    fun observeAirConditionUserCookie(): Flow<ACCookie>
    fun observeBookSearchHistoryList(): Flow<List<String>>
    fun observeJWCToken(): Flow<String>
    fun observeLoginJWCState(): Flow<Int>
    fun observeReadNoticeIdList(): Flow<List<Int>>
    fun observeGlobalTermCode(): Flow<String>
    fun observeMobileCode(): Flow<String>
    fun observeIsWriteCalendarPermissionGranted(): Flow<Boolean>
    fun observeAIFunctionEnabled(): Flow<Boolean>
    fun observeAIModelConfig(): Flow<String>
    fun observeBionicReadingEnabled(): Flow<Boolean>
    fun observeLoadImgEnabled(): Flow<Boolean>
    fun observeNewsHistoryList(): Flow<List<NewsMarkEntity>>
    fun observeNewsFavoriteList(): Flow<List<NewsMarkEntity>>
    fun observeNewsFontSize(): Flow<Int>

}