package com.example.composetrainer.ui.screens.invoice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composetrainer.ui.viewmodels.InvoiceViewModel
import com.example.composetrainer.ui.viewmodels.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoiceScreen(
    invoiceViewModel: InvoiceViewModel = hiltViewModel(),
    productViewModel: ProductsViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    val currentInvoice by invoiceViewModel.currentInvoice.collectAsState()
    val products by productViewModel.products.collectAsState()
    var showProductSelection by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Invoice") },
                navigationIcon = {
                    IconButton(onClick = onComplete) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showProductSelection = true },
                icon = { Icon(Icons.Default.Add, "Add Product") },
                text = { Text("Add Product") }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = "Total: $${invoiceViewModel.calculateTotalPrice()}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    invoiceViewModel.createInvoice()
                    onComplete()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = currentInvoice.isNotEmpty()
            ){
                Text("Create Invoice")
            }
        }
        if (showProductSelection){
            ProductSelectionBottomSheet(
                products = products,
                onAddToInvoice = { product , quantity ->
                    invoiceViewModel.addToCurrentInvoice(product, quantity)
                    showProductSelection = false
                },
                onDismiss = { showProductSelection = false }
            )
        }

    }
}