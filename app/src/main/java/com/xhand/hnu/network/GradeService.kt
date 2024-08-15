package com.xhand.hnu.network

import com.xhand.hnu.model.entity.BookBooks
import com.xhand.hnu.model.entity.BookDetailEntity
import com.xhand.hnu.model.entity.BookDetailPost
import com.xhand.hnu.model.entity.BookPost
import com.xhand.hnu.model.entity.BuildingEntiy
import com.xhand.hnu.model.entity.CheckTokenEntity
import com.xhand.hnu.model.entity.ClassroomEntity
import com.xhand.hnu.model.entity.ClassroomPost
import com.xhand.hnu.model.entity.CourseSearchEntity
import com.xhand.hnu.model.entity.CourseSearchIndexEntity
import com.xhand.hnu.model.entity.CourseSearchPost
import com.xhand.hnu.model.entity.CourseTaskEntity
import com.xhand.hnu.model.entity.GradeDetailEntity
import com.xhand.hnu.model.entity.GradeDetailPost
import com.xhand.hnu.model.entity.GradeDetailsEntity
import com.xhand.hnu.model.entity.GradeEntity
import com.xhand.hnu.model.entity.GradeIndexEntity
import com.xhand.hnu.model.entity.GradePost
import com.xhand.hnu.model.entity.HadBookDetailEntity
import com.xhand.hnu.model.entity.JDEntity
import com.xhand.hnu.model.entity.JDPost
import com.xhand.hnu.model.entity.MessageEntity
import com.xhand.hnu.model.entity.MessagePost
import com.xhand.hnu.model.entity.ReadMessageEntity
import com.xhand.hnu.model.entity.ReadMessagePost
import com.xhand.hnu.model.entity.TeacherEntity
import com.xhand.hnu.model.entity.teacherPost
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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
    ): TeacherEntity

    @POST("dev-api/appapi/getNotice")
    suspend fun messageDetails(
        @Body body: MessagePost,
        @Header("Token") token: String
    ): MessageEntity

    /*@GET("dev-api/appapi/appkxjs/buildingData")
    suspend fun buildingData(
        @Header("Token") token: String
    ): BuildingEntiy*/

    @POST("dev-api/appapi/appkxjs/classroom")
    suspend fun classroomData(
        @Body body: ClassroomPost,
        @Header("Token") token: String
    ): ClassroomEntity

    @POST("dev-api/appapi/Studentcj/cjjdDatas")
    suspend fun gradeJDDetail(
        @Body body: JDPost,
        @Header("Token") token: String
    ): JDEntity

    @POST("dev-api/appapi/Studentxsxdjc/xdjcdatas")
    suspend fun bookDetail(
        @Body body: BookPost,
        @Header("Token") token: String
    ): BookBooks

    @POST("dev-api/appapi/Studentxsxdjc/kxjcdatas")
    suspend fun bookDetail2(
        @Body body: BookDetailPost,
        @Header("Token") token: String
    ): BookDetailEntity

    @POST("dev-api/appapi/Studentxsxdjc/yxjcdatas")
    suspend fun bookDetail3(
        @Body body: BookDetailPost,
        @Header("Token") token: String
    ): HadBookDetailEntity

    @POST("dev-api/appapi/appqxkb/data")
    suspend fun courseSearch(
        @Body body: CourseSearchPost,
        @Header("Token") token: String
    ): CourseSearchEntity

    @POST("dev-api/appapi/Studentskrw/data")
    suspend fun courseTask(
        @Body body: GradePost,
        @Header("Token") token: String
    ): CourseTaskEntity

    @GET("dev-api/appapi/getIstoken")
    suspend fun checkToken(
        @Header("Token") token: String
    ): CheckTokenEntity

    @POST("dev-api/appapi/readed")
    suspend fun readMessage(
        @Body body: ReadMessagePost,
        @Header("Token") token: String
    ): ReadMessageEntity

    @GET("dev-api/appapi/appqxkb/index")
    suspend fun courseSearchIndex(
        @Header("Token") token: String
    ): CourseSearchIndexEntity


    @POST("dev-api/appapi/Studentcj/index")
    suspend fun gradeIndex(
        @Header("Token") token: String
    ): GradeIndexEntity

    companion object {
        fun instance(): GradeService {
            return Network.loginService(GradeService::class.java)
        }
    }

}