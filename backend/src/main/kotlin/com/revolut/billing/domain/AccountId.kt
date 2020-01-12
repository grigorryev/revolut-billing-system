package com.revolut.billing.domain

class AccountId(
    val type: AccountType,
    val subjectId: String,
    val currency: String
)