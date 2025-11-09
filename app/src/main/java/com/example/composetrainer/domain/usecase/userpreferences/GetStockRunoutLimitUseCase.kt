package com.example.composetrainer.domain.usecase.userpreferences

import com.example.composetrainer.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStockRunoutLimitUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Int> = repository.stockRunoutLimit
}