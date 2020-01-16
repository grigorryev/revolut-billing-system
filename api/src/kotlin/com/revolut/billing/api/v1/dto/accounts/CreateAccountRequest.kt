package com.revolut.billing.api.v1.dto.accounts

data class CreateAccountRequest(
    /**
     * Type of account.
     */
    val type: AccountType,

    /**
     * /**
     * Identifier of account's "owner".
     * `userId` for user accounts, `paymentSystemId` for accounts representing payment systems.
    */
     */
    val subjectId: String,

    /**
     * Standard 3-letter currency name.
     */
    val currency: String
)