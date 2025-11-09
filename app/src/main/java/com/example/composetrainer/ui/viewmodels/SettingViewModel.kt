package com.example.composetrainer.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrainer.domain.model.Barcode
import com.example.composetrainer.domain.model.InvoiceProductFactory
import com.example.composetrainer.domain.model.InvoiceWithProducts
import com.example.composetrainer.domain.model.ProductFactory
import com.example.composetrainer.domain.model.ProductName
import com.example.composetrainer.domain.model.Quantity
import com.example.composetrainer.domain.model.StockQuantity
import com.example.composetrainer.domain.model.autoCreateInvoice
import com.example.composetrainer.domain.model.type.Money
import com.example.composetrainer.domain.usecase.invoice.InitInvoiceWithProductsUseCase
import com.example.composetrainer.domain.usecase.invoice.InsertInvoiceUseCase
import com.example.composetrainer.domain.usecase.product.AddProductUseCase
import com.example.composetrainer.domain.usecase.product.EditProductUseCase
import com.example.composetrainer.domain.usecase.product.GetProductByQueryUseCase
import com.example.composetrainer.domain.usecase.userpreferences.GetStockRunoutLimitUseCase
import com.example.composetrainer.domain.usecase.userpreferences.SaveStockRunoutLimitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getStockRunoutLimitUseCase: GetStockRunoutLimitUseCase,
    private val saveStockRunoutLimitUseCase: SaveStockRunoutLimitUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _stockRunoutLimit = MutableStateFlow(5)
    val stockRunoutLimit: StateFlow<Int> = _stockRunoutLimit

    init {
        observeStockRunoutLimit()
    }

    private fun observeStockRunoutLimit() {
        viewModelScope.launch {
            getStockRunoutLimitUseCase().collectLatest { limit ->
                _stockRunoutLimit.value = limit
            }
        }
    }

    fun saveStockRunoutLimit(limit: Int) {
        viewModelScope.launch {
            saveStockRunoutLimitUseCase(limit)
        }
    }


}