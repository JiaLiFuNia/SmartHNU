package com.smart.htu.repo

import android.content.Context
import android.util.Log
import com.smart.htu.api.module.BuildingEntity
import com.smart.htu.api.module.ClassroomOccupationEntity
import com.smart.htu.api.network.GiteeService
import com.smart.htu.api.network.JWCService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class JWCNetworkRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val jwcService: JWCService,
    private val giteeService: GiteeService,
    private val dataStoreRepo: DataStoreRepo
) {

    // 教室查询
    suspend fun getClassroomOccupationService(
        building: BuildingEntity,
        token: String
    ): Result<ClassroomOccupationEntity> {
        try {
            val res = jwcService.classroomOccupation(building, token)
            return when (res.code) {
                200 -> Result.success(res)
                401 -> Result.failure(Exception("401"))
                else -> Result.failure(Exception("获取失败"))
            }
        } catch (e: Exception) {
            Log.e("TAG666", "${e.message}")
            return Result.failure(Exception("获取失败"))
        }
    }

}