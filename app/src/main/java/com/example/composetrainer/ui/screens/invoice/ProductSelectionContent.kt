package com.example.composetrainer.ui.screens.invoice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.ui.components.ProductSelectionItem

@Composable
fun ProductSelectionContent(
    products: List<Product>,
    onProductSelected: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Select a Product",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    ProductSelectionList(
        products = products,
        onProductSelected = onProductSelected
    )
}

@Composable
private fun ProductSelectionList(
    products: List<Product>,
    onProductSelected: (Product) -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    LazyColumn(
        modifier = Modifier.heightIn(max = screenHeight * 0.5f),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products, key = { it.id }) { product ->
            ProductSelectionItem(
                product = product,
                onClick = { onProductSelected(product) }
            )
        }
    }
}