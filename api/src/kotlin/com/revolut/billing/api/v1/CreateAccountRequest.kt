package com.revolut.billing.api.v1

data class CreateAccountRequest(
    val currency: String,
    val details: String
)