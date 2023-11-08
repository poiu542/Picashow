package io.b306.picashow.api.image

import com.google.gson.annotations.SerializedName

data class ImageItem(
    @SerializedName("url")
    val url: String
)
