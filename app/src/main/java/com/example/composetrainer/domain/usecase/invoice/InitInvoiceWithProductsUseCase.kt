package com.example.composetrainer.domain.usecase.invoice

import android.util.Log
import com.example.composetrainer.domain.model.InvoiceFactory
import com.example.composetrainer.domain.model.InvoiceId
import com.example.composetrainer.domain.model.InvoiceNumber
import com.example.composetrainer.domain.model.InvoiceWithProducts
import com.example.composetrainer.domain.repository.InvoiceRepository
import javax.inject.Inject


class InitInvoiceWithProductsUseCase @Inject constructor(
    private val getInvoiceNumberUseCase: GetInvoiceNumberUseCase
) {

    suspend operator fun invoke(): InvoiceWithProducts {
        Log.i("InitInvoiceWithProductsUseCase", "invoke: InitInvoiceWithProductsUseCase ")
        return InvoiceWithProducts.createDefault(
            invoiceId = InvoiceId(getInvoiceNumberUseCase.invoke()), // Change to Real InvoiceId later on Insert To DB
            invoiceNumber = InvoiceNumber(
                getInvoiceNumberUseCase.invoke()
            )
        )
    }
}