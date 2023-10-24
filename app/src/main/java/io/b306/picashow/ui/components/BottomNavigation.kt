package io.b306.picashow.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//class BottomNavigation {
//}
data class BottomNavigationItem(
    val icon: @Composable () -> Unit,
    val selected: Boolean,
    val onClick: () -> Unit
)



@Composable
fun BottomNavigation(modifier: Modifier = Modifier, items: List<BottomNavigationItem>) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items.forEach { item ->
            BottomNavigationItemComposable(
                icon = item.icon,
                selected = item.selected,
                onClick = item.onClick
            )
        }
    }
}

@Composable
fun BottomNavigationItemComposable(
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        icon()
        if (selected) {
            // 선택된 아이템을 시각적으로 표시하기 위한 로직 (예: 아이콘의 색 변경)
        }
    }
}

