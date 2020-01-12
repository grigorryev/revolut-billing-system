package com.revolut.billing.api.v1.dto.accounts

data class CreateAccountRequest(
    val currency: String,
    val details: String
)