package io.b101.picashow.api.image

data class ImageRequest(
    val client_id : String
)

data class CreateImageRequest(
    val input_text : String,
    val user_theme : String
)
