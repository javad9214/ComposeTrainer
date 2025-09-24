package com.example.composetrainer.ui.screens.productlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.composetrainer.R
import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.domain.model.ProductId
import com.example.composetrainer.domain.model.SubcategoryId
import com.example.composetrainer.domain.model.Barcode
import com.example.composetrainer.domain.model.type.Money
import com.example.composetrainer.domain.model.StockQuantity
import com.example.composetrainer.domain.model.ProductName
import com.example.composetrainer.utils.barcode.BarcodeGenerator
import com.example.composetrainer.utils.price.ThousandSeparatorTransformation
import java.time.LocalDateTime

@Composable
fun AddProductBottomSheet(
    initialProduct: Product? = null, // Null = Add mode, Non-null = Edit mode
    onSave: (Product) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialProduct?.name?.value ?: "") }
    var barcode by remember { mutableStateOf(initialProduct?.barcode?.value ?: "") }
    var price by remember { mutableStateOf(initialProduct?.price?.amount?.toString() ?: "") }
    var subcategoryId by remember {
        mutableStateOf(
            initialProduct?.subcategoryId?.value?.toString() ?: ""
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    id = if (initialProduct == null) R.string.add_product_title else R.string.edit_product_title
                ),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Divider(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Product Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.product_name)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Barcode
            OutlinedTextField(
                value = barcode,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.length <= 12) {
                        barcode = newValue
                    }
                },
                label = { Text(stringResource(R.string.barcode_optional)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Price
            OutlinedTextField(
                value = price,
                onValueChange = { newText ->
                    // store raw number (without commas) in state
                    price = newText.replace(",", "")
                },
                label = { Text(stringResource(R.string.price_optional)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                visualTransformation = ThousandSeparatorTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subcategory ID
            OutlinedTextField(
                value = subcategoryId,
                onValueChange = { subcategoryId = it },
                label = { Text(stringResource(R.string.category_id_optional)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val newProduct = Product(
                        id = initialProduct?.id ?: ProductId(0),
                        name = ProductName(name),
                        barcode = if (barcode.isEmpty()) Barcode(BarcodeGenerator.generateBarcodeNumber()) else Barcode(
                            barcode
                        ),
                        price = price.toLongOrNull()?.let { Money(it) } ?: Money(0),
                        image = initialProduct?.image,
                        subcategoryId = subcategoryId.toIntOrNull()?.let { SubcategoryId(it) },
                        date = initialProduct?.date ?: LocalDateTime.now(),
                        stock = initialProduct?.stock ?: StockQuantity(0),
                        costPrice = initialProduct?.costPrice ?: Money(0),
                        description = initialProduct?.description,
                        supplierId = initialProduct?.supplierId,
                        unit = initialProduct?.unit,
                        minStockLevel = initialProduct?.minStockLevel,
                        maxStockLevel = initialProduct?.maxStockLevel,
                        isActive = initialProduct?.isActive ?: true,
                        tags = initialProduct?.tags,
                        lastSoldDate = initialProduct?.lastSoldDate,
                        synced = false,
                        createdAt = initialProduct?.createdAt ?: LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                    onSave(newProduct)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = name.isNotBlank()
            ) {
                Text(
                    text = stringResource(
                        id = if (initialProduct == null) R.string.add_product_button else R.string.save_changes_button
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
