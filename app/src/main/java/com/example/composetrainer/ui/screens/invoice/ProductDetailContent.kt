package com.example.composetrainer.ui.screens.invoice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.ui.components.ProductDetailHeader
import com.example.composetrainer.ui.components.QuantityStepper

@Composable
fun ProductDetailContent(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onBack: () -> Unit,
    onAddToInvoice: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ProductDetailHeader(product = product, onBack = onBack)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            QuantityStepper(
                value = quantity,
                onValueChange = onQuantityChange,
                min = 1,
                max = product.stock
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = onAddToInvoice,
            modifier = Modifier.fillMaxWidth(),
            enabled = quantity in 1..product.stock
        ) {
            Text("Add ${quantity}x to Invoice")
        }
    }
}