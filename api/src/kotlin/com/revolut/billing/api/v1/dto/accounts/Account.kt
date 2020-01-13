package com.revolut.billing.api.v1.dto.accounts

import java.math.BigDecimal

class Account(
    val accountId: AccountId,
    val amount: BigDecimal
)