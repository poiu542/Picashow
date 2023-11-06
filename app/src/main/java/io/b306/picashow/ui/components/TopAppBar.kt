package io.b306.picashow.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopAppBar(
    title: String,
    showIcon: Boolean = true,
    endContent: @Composable (Modifier) -> Unit // endContent는 @Composable 람다식으로 받습니다.
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp), // Icon을 포함한 모든 요소의 상하 패딩을 조절합니다.
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // Row 내의 모든 요소를 세로 중앙 정렬합니다.
    ) {
        if (showIcon) {
            Icon(
                Icons.Default.List,
                contentDescription = null,
                tint = Color.White
            )
        } else {
            Box {}
        }

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterVertically) // Text를 세로 중앙에 위치시킵니다.
        )

        endContent(Modifier) // 여기에 추가적인 offset 없이 Modifier를 그대로 전달합니다.
    }
}