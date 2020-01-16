package com.revolut.billing.api.v1.dto.accounts

/**
 * Unique identifier of the account.
 */
data class AccountId(
    /**
     * Type of the account.
     */
    val type: AccountType,

    /**
     * Identifier of account's "owner".
     * `userId` for user accounts, `paymentSystemId` for accounts representing payment systems.
     */
    val subjectId: String,

    /**
     * Standard 3-letter currency name.
     */
    val currency: String
)