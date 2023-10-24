package io.b306.picashow

import androidx.compose.runtime.Composable

data class BottomNavigationItem(
    val icon: @Composable () -> Unit,
    val selected: Boolean,
    val onClick: () -> Unit
)
