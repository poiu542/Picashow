package io.b306.picashow.api.image

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ImageAPI {

    // 일일 계획에 대한 이미지를 가져옵니다.
    @GET("image/daily")
    suspend fun getDailyImages(@Body request: ImageRequest): Response<List<ImageResponse>>

    // 서버에 등록된 전체 이미지를 가져옵니다.
    @GET("image/all")
    suspend fun getAllImages(@Body request: ImageRequest): Response<List<ImageResponse>>
}