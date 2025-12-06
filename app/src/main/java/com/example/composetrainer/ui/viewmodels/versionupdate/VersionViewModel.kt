package com.example.composetrainer.ui.viewmodels.versionupdate


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrainer.data.remote.dto.response.UpdateStatus
import com.example.composetrainer.domain.usecase.versionupdate.CheckAppVersionUseCase
import com.example.composetrainer.utils.VersionUtils
import com.example.login.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing app version checking and updates
 */
@HiltViewModel
class VersionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val checkAppVersionUseCase: CheckAppVersionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VersionUiState())
    val uiState: StateFlow<VersionUiState> = _uiState.asStateFlow()

    private val _events = Channel<VersionEvent>()
    val events = _events.receiveAsFlow()

    init {
        // Get current version info
        val versionCode = VersionUtils.getVersionCode(context)
        val versionName = VersionUtils.getVersionName(context)

        _uiState.update {
            it.copy(
                currentVersionCode = versionCode,
                currentVersionName = versionName
            )
        }
    }

    /**
     * Check for app updates
     * @param showDialogOnUpdate If true, shows dialog when update is available
     */
    fun checkForUpdates(showDialogOnUpdate: Boolean = true) {
        viewModelScope.launch {
            _events.send(VersionEvent.UpdateCheckStarted)

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            checkAppVersionUseCase(
                currentVersionCode = _uiState.value.currentVersionCode
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        val (versionInfo, updateStatus) = result.data

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                versionInfo = versionInfo,
                                updateStatus = updateStatus,
                                showUpdateDialog = showDialogOnUpdate &&
                                        (updateStatus == UpdateStatus.UPDATE_REQUIRED ||
                                                updateStatus == UpdateStatus.UPDATE_AVAILABLE),
                                errorMessage = null
                            )
                        }

                        _events.send(VersionEvent.UpdateCheckCompleted)

                        if (_uiState.value.showUpdateDialog) {
                            _events.send(VersionEvent.DialogShown)
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message
                            )
                        }
                        _events.send(VersionEvent.UpdateCheckFailed(result.message))
                    }
                }
            }
        }
    }

    /**
     * Silently check for updates without showing dialog
     * Useful for background checks
     */
    fun silentUpdateCheck() {
        checkForUpdates(showDialogOnUpdate = false)
    }

    /**
     * Show the update dialog manually
     */
    fun showUpdateDialog() {
        _uiState.update { it.copy(showUpdateDialog = true) }
        viewModelScope.launch {
            _events.send(VersionEvent.DialogShown)
        }
    }

    /**
     * Dismiss the update dialog
     * Only allowed if update is not required
     */
    fun dismissUpdateDialog() {
        if (!_uiState.value.isUpdateRequired) {
            _uiState.update { it.copy(showUpdateDialog = false) }
            viewModelScope.launch {
                _events.send(VersionEvent.DialogDismissed)
            }
        }
    }

    /**
     * User clicked the update button
     */
    fun onUpdateClicked() {
        viewModelScope.launch {
            _events.send(VersionEvent.UpdateClicked)
        }
    }

    /**
     * User clicked skip update (only for optional updates)
     */
    fun onSkipUpdate() {
        if (_uiState.value.canSkipUpdate) {
            dismissUpdateDialog()
            viewModelScope.launch {
                _events.send(VersionEvent.SkipClicked)
            }
        }
    }

    /**
     * Reset error state
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Get the update URL from version info
     */
    fun getUpdateUrl(): String? {
        return _uiState.value.versionInfo?.updateUrl
    }

    /**
     * Check if an update check is needed
     * Can be used to implement logic like "check once per day"
     */
    fun shouldCheckForUpdates(): Boolean {
        // TODO: Implement logic based on last check time
        // For now, always return true
        return true
    }
}