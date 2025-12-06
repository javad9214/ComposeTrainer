package com.example.composetrainer.data.remote.api

import com.example.composetrainer.data.remote.dto.response.AppVersionResponseDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * API Service for app version checking
 */
interface ApiServiceVersion {

    /**
     * Get version configuration for a specific platform
     * GET /api/version/admin/{platform}
     *
     * @param platform "ANDROID" or "IOS"
     * @return AppVersionResponseDto with version information
     */
    @GET(ApiConstants.API + "version/admin/{platform}")
    suspend fun getVersionByPlatform(
        @Path("platform") platform: String
    ): ApiResponse<AppVersionResponseDto>

    /**
     * Get all version configurations
     * GET /api/version/admin/all
     */
    @GET(ApiConstants.API + "version/admin/all")
    suspend fun getAllVersions(): ApiResponse<List<AppVersionResponseDto>>

    companion object {
        const val PLATFORM_ANDROID = "ANDROID"
        const val PLATFORM_IOS = "IOS"
    }
}