package com.revolut.billing.api.v1.dto.accounts

/**
 * Type of the account.
 */
enum class AccountType {
    /**
     * User's account.
     */
    MAIN_USER_ACCOUNT,

    /**
     * Technical type of account for representing payment systems.
     */
    PAYMENT_SYSTEM,

    /**
     * Account for transfer commission processing.
     */
    TRANSFER_COMMISSION
}