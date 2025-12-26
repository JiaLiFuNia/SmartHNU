package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.TermIndexEntity
import com.smart.htu.api.network.AppService
import com.smart.htu.api.network.JWCAppService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    suspend fun setJWCLoginState(state: Int)
    suspend fun getTermIndex(termCode: GlobalTerm = GlobalTerm()): Result<TermIndexEntity>
}

@Singleton
class SharedDataRepoImpl @Inject constructor(
    private val jwcAppService: JWCAppService,
    private val appService: AppService,
    private val dataStoreRepo: DataStoreRepo
) : SharedDataRepository {

    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val loginJWCState = dataStoreRepo.observeLoginJWCState()
        .stateIn(
            scope = scope,
            started = Eagerly,
            initialValue = runBlocking {
                dataStoreRepo.observeLoginJWCState().first()
            }
        )
    override val termIndex = MutableStateFlow<TermIndexEntity?>(null)

    override suspend fun setJWCLoginState(state: Int) {
        dataStoreRepo.changeLoginJWCState(state)
    }

    // 学期
    override suspend fun getTermIndex(termCode: GlobalTerm): Result<TermIndexEntity> {
        try {
            val res = jwcAppService.getTermIndex(termCode)
            Log.i("TAG666 shared", "获取学期成功")
            return when (res.code) {
                200 -> {
                    termIndex.value = res
                    termCode.termCode?.let {
                        dataStoreRepo.setGlobalTermCode(it)
                    }
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