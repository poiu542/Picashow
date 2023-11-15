package io.b101.picashow.api.image

data class CreateImageResponse(
    val statusCode: Int,
    val detail: String,
    val headers: Any? // 필요한 경우 이 부분을 적절한 타입으로 변경
)