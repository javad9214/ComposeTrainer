package com.example.composetrainer.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrainer.domain.usecase.servermainproduct.GetAllMainProductsUseCase
import com.example.composetrainer.domain.util.Resource
import com.example.composetrainer.ui.screens.productlist.ProductsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainProductsViewModel @Inject constructor(
    private val getAllMainProductsUseCase: GetAllMainProductsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init {
        loadProducts(reset = true)
    }

    private fun loadProducts(reset: Boolean = false) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val page = if (reset) 0 else currentState.currentPage

            // Update loading state
            _uiState.update { state ->
                state.copy(
                    isLoading = reset,
                    isLoadingMore = !reset,
                    error = null
                )
            }

            getAllMainProductsUseCase(page).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Loading state already set above
                    }

                    is Resource.Success -> {
                        val pagedResponse = resource.data

                        _uiState.update { state ->
                            state.copy(
                                products = if (reset) {
                                    pagedResponse.content
                                } else {
                                    state.products + pagedResponse.content
                                },
                                isLoading = false,
                                isLoadingMore = false,
                                error = null,
                                hasMorePages = !pagedResponse.last,
                                currentPage = if (pagedResponse.content.isNotEmpty()) {
                                    page + 1
                                } else {
                                    state.currentPage
                                }
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadMoreProducts() {
        val currentState = _uiState.value
        if (!currentState.isLoading &&
            !currentState.isLoadingMore &&
            currentState.hasMorePages) {
            loadProducts(reset = false)
        }
    }

    fun retry() {
        loadProducts(reset = true)
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

}