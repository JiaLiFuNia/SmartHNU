package com.smart.htu.repo

import android.util.Log
import com.smart.htu.api.module.GiteeEntity
import com.smart.htu.api.module.GlobalTerm
import com.smart.htu.api.module.TermIndex
import com.smart.htu.api.network.GiteeService
import com.smart.htu.api.network.JWCService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface SharedDataRepository {
    val giteeConfig: StateFlow<GiteeEntity?>
    val termIndex: StateFlow<TermIndex?>

    suspend fun getGiteeConfig(): Result<GiteeEntity>
    suspend fun getTermIndex(termCode: GlobalTerm = GlobalTerm()): Result<TermIndex>
}

@Singleton
class SharedDataRepoImpl @Inject constructor(
    private val jwcService: JWCService,
    private val giteeService: GiteeService,
    private val jwcNetworkRepo: JWCNetworkRepo,
    private val dataStoreRepo: DataStoreRepo
) : SharedDataRepository {

    override val giteeConfig = MutableStateFlow<GiteeEntity?>(null)

    override val termIndex = MutableStateFlow<TermIndex?>(null)


    // 获取gitee配置
    override suspend fun getGiteeConfig(): Result<GiteeEntity> {
        try {
            val config = giteeService.getGiteeConfig()
            giteeConfig.value = config
            dataStoreRepo.setGlobalTermCode(config.termCode)
            Log.i("TAG666 gitee", "获取配置成功")
            return Result.success(config)
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception("获取失败"))
        }
    }


    // 学期
    override suspend fun getTermIndex(termCode: GlobalTerm): Result<TermIndex> {
        try {
            val res = jwcService.getTermIndex(termCode)
            Log.i("TAG666 shared", "获取学期成功")
            return when (res.code) {
                200 -> {
                    termIndex.value = res
                    Result.success(res)
                }

                401 -> {
                    if (jwcNetworkRepo.reLogin())
                        getTermIndex(termCode)
                    else
                        Result.failure(Exception("401"))
                }

                else -> Result.failure(Exception("false"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(e)
        }
    }

}