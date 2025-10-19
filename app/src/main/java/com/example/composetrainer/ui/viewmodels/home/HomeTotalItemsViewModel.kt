package com.example.composetrainer.ui.viewmodels.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.domain.model.ProductSalesSummary
import com.example.composetrainer.domain.model.type.Money
import com.example.composetrainer.domain.usecase.analytics.GetInvoiceReportCountUseCase
import com.example.composetrainer.domain.usecase.analytics.GetTotalProfitPriceUseCase
import com.example.composetrainer.domain.usecase.analytics.GetTotalSoldPriceUseCase
import com.example.composetrainer.domain.usecase.sales.GetTopProfitableProductsUseCase
import com.example.composetrainer.domain.usecase.sales.GetTopSellingProductsUseCase
import com.example.composetrainer.utils.dateandtime.TimeRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeTotalItemsViewModel @Inject constructor(
    private val getInvoiceReportCountUseCase: GetInvoiceReportCountUseCase,
    private val getTotalSoldPriceUseCase: GetTotalSoldPriceUseCase,
    private val getTotalProfitPriceUseCase: GetTotalProfitPriceUseCase,
    private val getTopSellingProductsUseCase: GetTopSellingProductsUseCase,
    private val getTopProfitableProductsUseCase: GetTopProfitableProductsUseCase
) : ViewModel() {

    private val _totalInvoiceCount = MutableStateFlow(0)
    val totalInvoiceCount: StateFlow<Int> get() = _totalInvoiceCount

    private val _totalSoldPrice = MutableStateFlow(Money(0))
    val totalSoldPrice: StateFlow<Money> get() = _totalSoldPrice

    private val _totalProfitPrice = MutableStateFlow(Money(0))
    val totalProfitPrice: StateFlow<Money> get() = _totalProfitPrice

    private val _topSellingProductList = MutableStateFlow<List<ProductSalesSummary>>(emptyList())
    val topSellingProductList: StateFlow<List<ProductSalesSummary>> get() = _topSellingProductList

    private val _topProfitableProductList = MutableStateFlow<List<ProductSalesSummary>>(emptyList())
    val topProfitableProductList: StateFlow<List<ProductSalesSummary>> get() = _topProfitableProductList

    private val _mostSoldProducts = MutableStateFlow<List<Product>>(emptyList())
    val mostSoldProducts: StateFlow<List<Product>> get() = _mostSoldProducts


    private val _mostProfitableProducts = MutableStateFlow<List<Product>>(emptyList())
    val mostProfitableProducts: StateFlow<List<Product>> get() = _mostProfitableProducts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        loadAnalyticsData(TimeRange.TODAY)
        loadProductSalesSummary(TimeRange.TODAY)
    }

    private fun loadAnalyticsData(timeRange: TimeRange) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                launch {
                    getInvoiceReportCountUseCase.invoke(timeRange).collect {
                        _totalInvoiceCount.value = it
                    }
                }
                launch {
                    getTotalSoldPriceUseCase.invoke(timeRange).collect {
                        _totalSoldPrice.value = Money(it)
                    }
                }
                launch {
                    getTotalProfitPriceUseCase.invoke(timeRange).collect {
                        _totalProfitPrice.value = Money(it)
                    }
                }


            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadProductSalesSummary(timeRange: TimeRange) {
        viewModelScope.launch {
            _isLoading.value = true
            try {

                launch {
                    getTopSellingProductsUseCase.invoke(timeRange)
                        .collect { (productsSaleSummeryList, products) ->
                            _topSellingProductList.value = productsSaleSummeryList
                            _mostSoldProducts.value = products
                        }
                }


                launch {
                    getTopProfitableProductsUseCase.invoke(timeRange)
                        .collect { (productsSaleSummeryList, products) ->
                            Log.d(TAG, "loadProductSalesSummary: $productsSaleSummeryList")
                            _topProfitableProductList.value = productsSaleSummeryList
                            _mostProfitableProducts.value = products
                        }
                }


            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun reLoadProductSaleSummary(timeRange: TimeRange) {
        loadProductSalesSummary(timeRange)
        loadAnalyticsData(timeRange)
    }

    companion object {
        const val TAG = "HomeTotalItemsViewModel"
    }
}