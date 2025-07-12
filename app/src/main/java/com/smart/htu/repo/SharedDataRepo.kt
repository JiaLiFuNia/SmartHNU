package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.NoticeRes
import com.smart.htu.api.module.TermIndexEntity
import com.smart.htu.api.module.UpdateEntity
import com.smart.htu.api.network.AppService
import com.smart.htu.api.network.JWCService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

interface SharedDataRepository {
    val loginJWCState: StateFlow<Int>
    val termIndex: StateFlow<TermIndexEntity?>
    val notice: StateFlow<NoticeRes?>
    val update: StateFlow<UpdateEntity?>

    suspend fun setJWCLoginState(state: Int)
    suspend fun getTermIndex(termCode: GlobalTerm = GlobalTerm()): Result<TermIndexEntity>
    suspend fun getNotice(): Result<NoticeRes>
    suspend fun getUpdate(): Result<UpdateEntity>
}

@Singleton
class SharedDataRepoImpl @Inject constructor(
    private val jwcService: JWCService,
    private val appService: AppService,
    private val dataStoreRepo: DataStoreRepo
) : SharedDataRepository {

    val scope = CoroutineScope(Dispatchers.IO)

    override val loginJWCState = dataStoreRepo.observeLoginJWCState()
        .stateIn(
            scope = scope,
            started = Eagerly,
            initialValue = runBlocking {
                dataStoreRepo.observeLoginJWCState().first()
            }
        )
    override val termIndex = MutableStateFlow<TermIndexEntity?>(null)
    override val notice = MutableStateFlow<NoticeRes?>(null)
    override val update = MutableStateFlow<UpdateEntity?>(null)

    override suspend fun setJWCLoginState(state: Int) {
        dataStoreRepo.changeLoginJWCState(state)
    }

    override suspend fun getUpdate(): Result<UpdateEntity> {
        try {
            val res = appService.getUpdate()
            when (res.code()) {
                200 -> {
                    val updateRes = res.body()
                    if (updateRes != null) {
                        update.value = updateRes.data
                        Log.i("TAG666", "获取更新成功")
                        return Result.success(updateRes.data)
                    } else {
                        return Result.failure(Exception("null"))
                    }
                }

                else -> {
                    Log.i("TAG666", "获取更新失败")
                    return Result.failure(Exception("获取失败，请切换至移动网络后重试"))
                }
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }


    override suspend fun getNotice(): Result<NoticeRes> {
        try {
            val res = appService.getNotice()
            when (res.code()) {
                200 -> {
                    val noticeRes = res.body()
                    if (noticeRes != null) {
                        notice.value = noticeRes
                        Log.i("TAG666", "获取公告成功")
                        return Result.success(noticeRes)
                    } else {
                        return Result.failure(Exception("null"))
                    }
                }

                else -> {
                    Log.i("TAG666", "获取公告失败")
                    return Result.failure(Exception("获取失败，请切换至移动网络后重试"))
                }
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

    // 学期
    override suspend fun getTermIndex(termCode: GlobalTerm): Result<TermIndexEntity> {
        try {
            val res = jwcService.getTermIndex(termCode)
            Log.i("TAG666 shared", "获取学期成功")
            return when (res.code) {
                200 -> {
                    termIndex.value = res
                    Result.success(res)
                }

                401 -> Result.failure(Exception(res.msg))

                else -> Result.failure(Exception(res.msg))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

}