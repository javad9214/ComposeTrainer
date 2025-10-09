package com.example.composetrainer.ui.screens.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.composetrainer.R
import com.example.composetrainer.ui.theme.Beirut_Medium
import com.example.composetrainer.utils.dimen
import com.example.composetrainer.utils.dimenTextSize
import com.example.composetrainer.utils.str


@Composable
fun TodayCard(
    modifier: Modifier,
    onSelected: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .height(dimen(R.dimen.size_lg))
                .wrapContentWidth()
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(dimen(R.dimen.radius_xxl)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            onClick = onSelected
        ) {

            Row(
                modifier = Modifier
                    .padding(dimen(R.dimen.space_1)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = dimen(R.dimen.space_1), start = dimen(R.dimen.space_4)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = str(R.string.today),
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = Beirut_Medium,
                        fontSize = dimenTextSize(R.dimen.text_size_lg)
                    )
                }

                Spacer(modifier = Modifier.width(dimen(R.dimen.space_1)))

                Icon(
                    modifier = Modifier.padding(end = dimen(R.dimen.space_1)),
                    painter = painterResource(id = R.drawable.keyboard_arrow_down_24px),
                    contentDescription = "down",
                )

            }
        }
    }
}
