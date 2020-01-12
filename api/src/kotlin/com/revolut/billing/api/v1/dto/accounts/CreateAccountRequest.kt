package com.revolut.billing.api.v1.dto.accounts

data class CreateAccountRequest(
    val type: AccountType,
    val subjectId: String,
    val currency: String
)