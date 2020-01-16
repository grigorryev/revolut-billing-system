package com.revolut.billing.api.v1.dto.accounts

import java.math.BigDecimal

/**
 * Represents an account in billing system.
 */
class Account(
    /**
     * Unique identifier of the account.
     */
    val accountId: AccountId,

    /**
     * Amount of money on the account. Can be negative for some special types of accounts.
     */
    val amount: BigDecimal
)