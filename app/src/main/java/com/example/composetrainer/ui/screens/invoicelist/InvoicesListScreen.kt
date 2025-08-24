package com.example.composetrainer.ui.screens.invoicelist

import android.app.AlertDialog as AndroidAlertDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composetrainer.R
import com.example.composetrainer.domain.model.Invoice
import com.example.composetrainer.domain.model.InvoiceWithProducts
import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.ui.theme.BComps
import com.example.composetrainer.ui.theme.BHoma
import com.example.composetrainer.ui.theme.Beirut_Medium
import com.example.composetrainer.ui.theme.ComposeTrainerTheme
import com.example.composetrainer.ui.viewmodels.InvoiceListViewModel
import com.example.composetrainer.utils.dimen
import com.example.composetrainer.utils.dimenTextSize
import com.example.composetrainer.utils.str

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoicesListScreen(
    invoiceListViewModel: InvoiceListViewModel = hiltViewModel(),
    onCreateNew: () -> Unit,
    onInvoiceClick: (Long) -> Unit
) {
    val invoices by invoiceListViewModel.invoices.collectAsState()
    val isLoading by invoiceListViewModel.isLoading.collectAsState()
    val errorMessage by invoiceListViewModel.errorMessage.collectAsState()
    val sortNewestFirst by invoiceListViewModel.sortNewestFirst.collectAsState()
    val isSelectionMode by invoiceListViewModel.isSelectionMode.collectAsState()
    val selectedInvoices by invoiceListViewModel.selectedInvoices.collectAsState()
    val showDeleteConfirmationDialog by invoiceListViewModel.showDeleteConfirmationDialog.collectAsState()

    val context = LocalContext.current

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        Column(
            modifier = Modifier
                .fillMaxSize()// or use WindowInsets.statusBars.asPaddingValues()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(start = dimen(R.dimen.space_6), end = dimen(R.dimen.space_2)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (isSelectionMode) {
                    Text(
                        text = if (selectedInvoices.isEmpty()) str(R.string.select_invoices_to_delete)
                        else str(R.string.selected_invoices_count).format(selectedInvoices.size),
                        fontFamily = BComps,
                        fontSize = dimenTextSize(R.dimen.text_size_lg)
                    )

                    Row {
                        // Delete button
                        IconButton(
                            onClick = {
                                if (selectedInvoices.isNotEmpty()) {
                                    invoiceListViewModel.showDeleteConfirmationDialog()
                                }
                            },
                            enabled = selectedInvoices.isNotEmpty()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete_24px),
                                contentDescription = str(R.string.delete),
                                tint = if (selectedInvoices.isEmpty())
                                    Color.Gray else MaterialTheme.colorScheme.error
                            )
                        }

                        // Cancel selection mode
                        IconButton(onClick = { invoiceListViewModel.toggleSelectionMode() }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = str(R.string.cancel)
                            )
                        }
                    }
                } else {
                    Text(
                        str(R.string.sale_invoices),
                        fontFamily = Beirut_Medium,
                        fontSize = dimenTextSize(R.dimen.text_size_xl)
                    )

                    Row {
                        // Long press hint
                        if (invoices.isNotEmpty()) {
                            IconButton(onClick = { invoiceListViewModel.toggleSelectionMode() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.delete_24px),
                                    contentDescription = str(R.string.delete),
                                    tint = Color.Gray
                                )
                            }
                        }

                        IconButton(onClick = { invoiceListViewModel.toggleSortOrder() }) {
                            Icon(
                                Icons.Default.Sort,
                                contentDescription = if (sortNewestFirst)
                                    "Sort oldest to newest" else "Sort newest to oldest"
                            )
                        }
                    }
                }


            }

            Box(modifier = Modifier.weight(1f)) {
                when {
                    isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                    errorMessage != null -> Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    invoices.isEmpty() -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No invoices available",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    else -> InvoicesLazyList(
                        invoiceWithProductsList = invoices,
                        onInvoiceClick = {
                            if (isSelectionMode) {
                                invoiceListViewModel.toggleInvoiceSelection(it)
                            } else {
                                onInvoiceClick(it)
                            }
                        },
                        onDelete = invoiceListViewModel::deleteInvoice,
                        onLongClick = {
                            if (!isSelectionMode) {
                                invoiceListViewModel.toggleSelectionMode()
                                invoiceListViewModel.toggleInvoiceSelection(it)
                            }
                        },
                        isSelectionMode = isSelectionMode,
                        selectedInvoices = selectedInvoices
                    )

                }
            }
        }


    }

    // Show delete confirmation dialog when needed
    if (showDeleteConfirmationDialog) {
        val context = LocalContext.current
        AndroidAlertDialog.Builder(context)
            .setTitle(context.getString(R.string.delete_selected_invoices))
            .setMessage(context.getString(R.string.are_you_sure_to_delete_selected_invoices))
            .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                invoiceListViewModel.deleteSelectedInvoices()
            }
            .setNegativeButton(context.getString(R.string.cancel)) { _, _ ->
                invoiceListViewModel.dismissDeleteConfirmationDialog()
            }
            .show()
    }
}

@Composable
private fun InvoicesLazyList(
    invoiceWithProductsList: List<InvoiceWithProducts>,
    onInvoiceClick: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onLongClick: (Long) -> Unit = {},
    isSelectionMode: Boolean = false,
    selectedInvoices: Set<Long> = emptySet()
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(invoiceWithProductsList, key = { it.invoiceId.value }) { invoice ->
                InvoiceItem(
                    invoiceWithProducts = invoice,
                    onClick = { onInvoiceClick(invoice.invoiceId.value) },
                    onDelete = { onDelete(invoice.invoiceId.value) },
                    onLongClick = { onLongClick(invoice.invoiceId.value) },
                    isSelected = selectedInvoices.contains(invoice.invoiceId.value),
                    isSelectionMode = isSelectionMode
                )
            }
        }
    }
}
