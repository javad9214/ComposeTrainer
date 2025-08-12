package com.example.composetrainer.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrainer.domain.model.Invoice
import com.example.composetrainer.domain.model.InvoiceNumber
import com.example.composetrainer.domain.model.InvoiceProduct
import com.example.composetrainer.domain.model.InvoiceWithProducts
import com.example.composetrainer.domain.model.Product
import com.example.composetrainer.domain.repository.InvoiceRepository
import com.example.composetrainer.domain.repository.ProductRepository
import com.example.composetrainer.domain.usecase.invoice.CheckProductStockUseCase
import com.example.composetrainer.domain.usecase.invoice.DeleteInvoiceUseCase
import com.example.composetrainer.domain.usecase.invoice.GetInvoiceNumberUseCase
import com.example.composetrainer.domain.usecase.invoice.InsertInvoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val productRepository: ProductRepository,
    private val deleteInvoiceUseCase: DeleteInvoiceUseCase,
    private val insertInvoiceUseCase: InsertInvoiceUseCase,
    private val getInvoiceNumberUseCase: GetInvoiceNumberUseCase,
    private val checkProductStockUseCase: CheckProductStockUseCase
): ViewModel() {

    private val _currentInvoice = MutableStateFlow<List<InvoiceProduct>>(emptyList())
    val currentInvoice: StateFlow<List<InvoiceProduct>> get() = _currentInvoice

    private val _selectedProducts = MutableStateFlow<List<Product>>(emptyList())
    val selectedProducts: StateFlow<List<Product>> get() = _selectedProducts

    private val _invoices = MutableStateFlow<List<Invoice>>(emptyList())
    val invoices: StateFlow<List<Invoice>> get() = _invoices

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _nextInvoiceNumber = MutableStateFlow<Long?>(null)
    val nextInvoiceNumber: StateFlow<Long?> get() = _nextInvoiceNumber

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())
    val filteredProducts: StateFlow<List<Product>> get() = _filteredProducts

    private val _sortNewestFirst = MutableStateFlow(true)
    val sortNewestFirst: StateFlow<Boolean> get() = _sortNewestFirst

    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode: StateFlow<Boolean> get() = _isSelectionMode

    private val _selectedInvoices = MutableStateFlow<Set<Long>>(emptySet())
    val selectedInvoices: StateFlow<Set<Long>> get() = _selectedInvoices

    private val _showDeleteConfirmationDialog = MutableStateFlow(false)
    val showDeleteConfirmationDialog: StateFlow<Boolean> get() = _showDeleteConfirmationDialog

    init {
        loadInvoices()
        loadProducts()
    }

    fun toggleSortOrder() {
        _sortNewestFirst.value = !_sortNewestFirst.value
        loadInvoices()
    }

    fun loadInvoices(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (_sortNewestFirst.value) {
                    invoiceRepository.getAllInvoices().collectLatest { invoices ->
                        _invoices.value = invoices.map { it.invoice }
                        _isLoading.value = false
                    }
                } else {
                    invoiceRepository.getAllInvoicesOldestFirst().collectLatest { invoices ->
                        _invoices.value = invoices.map { it.invoice }
                        _isLoading.value = false
                    }
                }
            }catch (e: Exception){
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                val query = _searchQuery.value
                if (query.isBlank()) {
                    productRepository.getAllProducts().collectLatest { productList ->
                        _products.value = productList
                        _filteredProducts.value = productList
                    }
                } else {
                    productRepository.searchProducts(query).collectLatest { productList ->
                        _products.value = productList
                        _filteredProducts.value = productList
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load products: ${e.message}"
            }
        }
    }

    fun addToCurrentInvoice(product: Product, quantity: Int){
        val existingItem = _currentInvoice.value.find { it.productId == product.id }
        val availableStock = product.stock
        val safeQuantityToAdd = if (quantity > availableStock) availableStock else quantity

        val updatedList = if (existingItem != null) {
            val newTotalQuantity = existingItem.quantity + safeQuantityToAdd
            val finalQuantity =
                if (newTotalQuantity > availableStock) availableStock else newTotalQuantity

            _currentInvoice.value.map {
                if (it.productId == product.id) {
                    it.copy(quantity = finalQuantity)
                } else it
            }
        }else {
            _currentInvoice.value + ProductWithQuantity(product, safeQuantityToAdd)
        }
        _currentInvoice.value = updatedList

    }

    fun removeFromCurrentInvoice(productId: Long){
        _currentInvoice.value = _currentInvoice.value.filter { it.product.id != productId }
    }

    fun createInvoice(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val invoiceNumber = getInvoiceNumberUseCase.invoke()
                val invoiceWithProducts = InvoiceWithProducts.createWithCalculatedTotals(
                        invoiceNumber = InvoiceNumber(invoiceNumber),
                        domainProducts = _products.value,
                        invoiceProducts = _currentInvoice.value
                )

                insertInvoiceUseCase.invoke(invoiceWithProducts)
                _currentInvoice.value = emptyList()
                loadInvoices() // Refresh invoice list
                _errorMessage.value = null
            }catch(e: Exception) {
                _errorMessage.value = "Failed to create invoice: ${e.message}"
            }finally {
                _isLoading.value = false
            }

        }
    }


    fun deleteInvoice(invoiceId: Long){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                deleteInvoiceUseCase(invoiceId)
                loadInvoices() // Refresh invoice list
                _errorMessage.value = null
            }catch (e: Exception){
                _errorMessage.value = "Failed to delete invoice: ${e.message}"
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteSelectedInvoices() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                deleteInvoiceUseCase.deleteMultiple(_selectedInvoices.value.toList())
                _selectedInvoices.value = emptySet()
                _isSelectionMode.value = false
                _showDeleteConfirmationDialog.value = false
                loadInvoices() // Refresh invoice list
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete selected invoices: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearCurrentInvoice(){
        _currentInvoice.value = emptyList()
    }

    fun updateItemQuantity(productId: Long, newQuantity: Int) {
        _currentInvoice.value = _currentInvoice.value.map {
            if (it.product.id == productId) {
                // Get available stock
                val availableStock = it.product.stock ?: 0
                // Ensure new quantity doesn't exceed stock
                val safeQuantity = if (newQuantity > availableStock) availableStock else newQuantity
                it.copy(quantity = safeQuantity)
            } else {
                it
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        loadProducts()
    }

    fun getNextInvoiceNumberId() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val nextNumber = invoiceRepository.getNextInvoiceNumberId()
                _nextInvoiceNumber.value = nextNumber
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to get next invoice number: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun calculateTotalPrice(): Long {
        return _currentInvoice.value.sumOf {
            it.product.price?.times(it.quantity) ?: 0L
        }
    }

    // Selection Mode Functions
    fun toggleSelectionMode() {
        _isSelectionMode.value = !_isSelectionMode.value
        if (!_isSelectionMode.value) {
            _selectedInvoices.value = emptySet()
        }
    }

    fun toggleInvoiceSelection(invoiceId: Long) {
        val currentSelection = _selectedInvoices.value.toMutableSet()
        if (currentSelection.contains(invoiceId)) {
            currentSelection.remove(invoiceId)
        } else {
            currentSelection.add(invoiceId)
        }
        _selectedInvoices.value = currentSelection
    }

    fun isInvoiceSelected(invoiceId: Long): Boolean {
        return _selectedInvoices.value.contains(invoiceId)
    }

    fun clearSelection() {
        _selectedInvoices.value = emptySet()
    }

    fun showDeleteConfirmationDialog() {
        _showDeleteConfirmationDialog.value = true
    }

    fun dismissDeleteConfirmationDialog() {
        _showDeleteConfirmationDialog.value = false
    }
}