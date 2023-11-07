package io.b306.picashow.api

import io.b306.picashow.api.image.ImageAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiObject {
    // 10.25. 미정된 상태
    private const val BASE_URL = "http://k9b101.p.ssafy.io:8000/"

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // BODY는 요청/응답의 헤더 및 본문을 모두 로그에 출력합니다.
        }
    }

    // API 요청시 log 띄우기
    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // API 요청
    private val getRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    val ImageService: ImageAPI by lazy {
        getRetrofit.create(ImageAPI::class.java)
    }


}