package com.xhand.hnu2.network

import com.xhand.hnu2.model.entity.GradeDetailEntity
import com.xhand.hnu2.model.entity.GradeDetailPost
import com.xhand.hnu2.model.entity.GradeDetailsEntity
import com.xhand.hnu2.model.entity.GradeEntity
import com.xhand.hnu2.model.entity.GradePost
import com.xhand.hnu2.model.entity.teacherEntity
import com.xhand.hnu2.model.entity.teacherPost
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface GradeService {
    @POST("dev-api/appapi/Studentcj/data")
    suspend fun gradePost(
        @Body body: GradePost,
        @Header("Token") token: String
    ): GradeEntity

    @POST("dev-api/appapi/Studentcj/ranking")
    suspend fun gradeDetail(
        @Body body: GradeDetailPost,
        @Header("Token") token: String
    ): GradeDetailEntity

    @POST("dev-api/appapi/Studentcj/detail")
    suspend fun gradeDetails(
        @Body body: GradeDetailPost,
        @Header("Token") token: String
    ): GradeDetailsEntity

    @POST("dev-api/appapi/Studentpjwj/teacher")
    suspend fun teacherDetails(
        @Body body: teacherPost,
        @Header("Token") token: String
    ): teacherEntity

    companion object {
        fun instance(): GradeService {
            return Network.loginService(GradeService::class.java)
        }
    }

}