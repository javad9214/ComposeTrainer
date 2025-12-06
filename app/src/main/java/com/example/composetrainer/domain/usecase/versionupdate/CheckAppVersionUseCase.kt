package com.example.composetrainer.domain.usecase.versionupdate



import com.example.composetrainer.data.remote.dto.response.AppVersionInfo
import com.example.composetrainer.data.remote.dto.response.UpdateStatus
import com.example.composetrainer.domain.repository.VersionRepository
import com.example.login.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for checking app version and updates
 */
class CheckAppVersionUseCase @Inject constructor(
    private val repository: VersionRepository
) {

    /**
     * Check if an update is available for the current app version
     *
     * @param currentVersionCode Current version code of the app
     * @return Flow of Resource containing version info and update status
     */
    suspend operator fun invoke(
        currentVersionCode: Int
    ): Flow<Resource<Pair<AppVersionInfo, UpdateStatus>>> {
        return repository.checkForUpdates(currentVersionCode)
    }
}

/**
 * Data class to hold version check result
 */
data class VersionCheckResult(
    val versionInfo: AppVersionInfo,
    val updateStatus: UpdateStatus,
    val currentVersionCode: Int
) {
    val isUpdateRequired: Boolean
        get() = updateStatus == UpdateStatus.UPDATE_REQUIRED

    val isUpdateAvailable: Boolean
        get() = updateStatus == UpdateStatus.UPDATE_AVAILABLE

    val shouldShowDialog: Boolean
        get() = isUpdateRequired || isUpdateAvailable
}