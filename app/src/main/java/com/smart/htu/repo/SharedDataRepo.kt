package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.NoticeEntity
import com.smart.htu.api.module.TermIndex
import com.smart.htu.api.module.UpdateData
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
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

interface SharedDataRepository {
    val termIndex: StateFlow<TermIndex?>
    val notice: MutableStateFlow<NoticeEntity?>
    val update: StateFlow<UpdateData?>

    suspend fun getTermIndex(termCode: GlobalTerm = GlobalTerm()): Result<TermIndex>
    suspend fun getNotice(): Result<NoticeEntity>
    suspend fun getUpdate(): Result<UpdateData>
}

@Singleton
class SharedDataRepoImpl @Inject constructor(
    private val jwcService: JWCService,
    private val appService: AppService,
    private val dataStoreRepo: DataStoreRepo
) : SharedDataRepository {

    val scope = CoroutineScope(Dispatchers.IO)

    override val termIndex = MutableStateFlow<TermIndex?>(null)
    override val notice = MutableStateFlow<NoticeEntity?>(null)
    override val update = MutableStateFlow<UpdateData?>(null)

    private val tokenValidity = dataStoreRepo.observeTokenValidity()
        .stateIn(
            scope = scope,
            started = Eagerly,
            initialValue = runBlocking {
                dataStoreRepo.observeTokenValidity().first()
            }
        )

    override suspend fun getUpdate(): Result<UpdateData> {
        try {
            val call = appService.getUpdate()
            val res = call.awaitResponse()
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


    override suspend fun getNotice(): Result<NoticeEntity> {
        try {
            val call = appService.getNotice()
            val res = call.awaitResponse()
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
    override suspend fun getTermIndex(termCode: GlobalTerm): Result<TermIndex> {
        try {
            if (!tokenValidity.value) {
                return Result.failure(Exception("token失效"))
            }
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