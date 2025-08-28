package com.example.composetrainer.ui.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composetrainer.domain.model.type.Money
import com.example.composetrainer.domain.usecase.analytics.GetInvoiceReportCountUseCase
import com.example.composetrainer.domain.usecase.analytics.GetTotalProfitPriceUseCase
import com.example.composetrainer.domain.usecase.analytics.GetTotalSoldPriceUseCase
import com.example.composetrainer.domain.usecase.product.GetProductByBarcodeUseCase
import com.example.composetrainer.utils.ProductImporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeTotalItemsViewModel @Inject constructor(
    private val getInvoiceReportCountUseCase: GetInvoiceReportCountUseCase,
    private val getTotalSoldPriceUseCase: GetTotalSoldPriceUseCase,
    private val getTotalProfitPriceUseCase: GetTotalProfitPriceUseCase
) : ViewModel() {



    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private fun loadAnalyticsData(){
        viewModelScope.launch {
            _isLoading.value = true
            try {



            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }
}