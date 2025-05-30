package com.example.composetrainer.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrainer.domain.model.AnalyticsData
import com.example.composetrainer.domain.usecase.analytics.GetAnalyticsDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyzeViewModel @Inject constructor(
    private val getAnalyticsDataUseCase: GetAnalyticsDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyzeUiState())
    val uiState: StateFlow<AnalyzeUiState> = _uiState.asStateFlow()

    init {
        loadAnalyticsData()
    }

    private fun loadAnalyticsData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                Log.d("AnalyzeViewModel", "Loading analytics data...")
                val analyticsData = getAnalyticsDataUseCase()
                Log.d("AnalyzeViewModel", "Analytics data loaded: $analyticsData")
                _uiState.value = _uiState.value.copy(
                    analyticsData = analyticsData,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                Log.e("AnalyzeViewModel", "Error loading analytics data", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun refresh() {
        loadAnalyticsData()
    }
}

data class AnalyzeUiState(
    val analyticsData: AnalyticsData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)