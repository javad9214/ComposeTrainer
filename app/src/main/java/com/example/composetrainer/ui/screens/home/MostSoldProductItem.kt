package com.example.composetrainer.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.composetrainer.R
import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.domain.model.ProductSalesSummary
import com.example.composetrainer.ui.theme.BMitra
import com.example.composetrainer.ui.theme.Beirut_Medium
import com.example.composetrainer.utils.PriceValidator.formatPrice
import com.example.composetrainer.utils.dimen
import com.example.composetrainer.utils.dimenTextSize
import com.example.composetrainer.utils.str

@Composable
fun MostSoldProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    productSalesSummary: ProductSalesSummary
) {


    ElevatedCard(
        modifier = modifier
            .padding(dimen(R.dimen.space_4))
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(dimen(R.dimen.radius_lg)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(dimen(R.dimen.space_2))) {

            Text(
                text = str(R.string.total_sales),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = Beirut_Medium,
                fontSize = dimenTextSize(R.dimen.text_size_lg)
            )


            InfoRow(
                modifier = Modifier,
                label = str(R.string.products_sold_count),
                value = productSalesSummary.totalSold.value.toString(),
                icon = R.drawable.view_in_ar_24px,
                iconDescription = str(R.string.products_sold_count)
            )

            InfoRow(
                modifier = Modifier,
                label = str(R.string.total_profit),
                value = productSalesSummary.totalRevenue.amount.toString(),
                icon = R.drawable.view_in_ar_24px,
                iconDescription = str(R.string.total_profit),
                isAmount = true
            )

            InfoRow(
                modifier = Modifier,
                label = str(R.string.total_amount),
                value = productSalesSummary.totalCost.amount.toString(),
                icon = R.drawable.view_in_ar_24px,
                iconDescription = str(R.string.total_amount),
                isAmount = true
            )

        }
    }
}


@Composable
private fun InfoRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: Int,
    iconDescription: String,
    isAmount: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimen(R.dimen.space_2)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            fontFamily = BMitra,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row {

            if (isAmount) {
                Row {
                    Text(
                        formatPrice(value),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.toman),
                        contentDescription = "toman",
                        modifier = Modifier
                            .size(dimen(R.dimen.size_sm))
                            .padding(start = dimen(R.dimen.space_1))
                    )
                }
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = BMitra,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Icon(
                painter = painterResource(id = icon),
                contentDescription = iconDescription,
                modifier = Modifier
                    .size(dimen(R.dimen.size_sm))
                    .padding(start = dimen(R.dimen.space_1))
            )

        }


    }
}

