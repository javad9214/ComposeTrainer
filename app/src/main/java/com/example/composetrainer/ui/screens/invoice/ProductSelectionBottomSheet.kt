package com.example.composetrainer.ui.screens.invoice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composetrainer.domain.model.Product

/**
 * A bottom sheet for selecting products for an invoice.
 *
 * @param products The list of available products
 * @param onAddToInvoice Callback when a product is added to the invoice with quantity
 * @param onDismiss Callback when the sheet is dismissed
 * @param sheetState Optional sheet state for controlling the bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSelectionBottomSheet(
    products: List<Product>,
    onAddToInvoice: (Product, Int) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var selectedProductId by rememberSaveable { mutableStateOf<Long?>(null) }
    var quantity by rememberSaveable { mutableIntStateOf(1) }
    val selectedProduct = selectedProductId?.let { id -> products.find { it.id == id } }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            if (selectedProduct == null) {
                ProductSelectionContent(
                    products = products,
                    onProductSelected = {
                        selectedProductId = it.id
                        quantity = 1 // reset on new product
                    }
                )
            } else {
                ProductDetailContent(
                    product = selectedProduct,
                    quantity = quantity,
                    onQuantityChange = { quantity = it },
                    onBack = { selectedProductId = null },
                    onAddToInvoice = { onAddToInvoice(selectedProduct, quantity); onDismiss() }
                )
            }
        }
    }
}