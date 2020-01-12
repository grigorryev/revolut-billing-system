package com.revolut.billing.api.v1.dto.accounts

import java.math.BigDecimal

class Account (
    val type: AccountType,
    val subjectId: String,
    val currency: String,
    val amount: BigDecimal
)