package com.example.composetrainer.domain.usecase.userpreferences


import com.example.composetrainer.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveStockRunoutLimitUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(limit: Int) {
        repository.saveStockRunoutLimit(limit)
    }
}