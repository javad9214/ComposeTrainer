package com.example.composetrainer.domain.model

data class Product(
    val id: Long,
    val name: String,
    val barcode: String?,
    val price: Long?,
    val image: String?,
    val categoryID: Int?,
    val date: Long,
    val stock: Int
)