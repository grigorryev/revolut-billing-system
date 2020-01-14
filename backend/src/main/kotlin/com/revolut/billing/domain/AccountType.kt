package com.revolut.billing.domain

import com.revolut.billing.api.v1.dto.accounts.AccountType as AccountTypeDto

enum class AccountType {
    MAIN_USER_ACCOUNT,
    PAYMENT_SYSTEM,
    TRANSFER_COMMISSION;

    fun canBeNegative(): Boolean {
        return this in setOf(
            PAYMENT_SYSTEM
        )
    }

    fun toDto(): AccountTypeDto = when (this) {
        MAIN_USER_ACCOUNT -> AccountTypeDto.MAIN_USER_ACCOUNT
        PAYMENT_SYSTEM -> AccountTypeDto.PAYMENT_SYSTEM
        TRANSFER_COMMISSION -> AccountTypeDto.TRANSFER_COMMISSION
    }

    companion object {
        fun fromDto(dto: AccountTypeDto) = when (dto) {
            AccountTypeDto.MAIN_USER_ACCOUNT -> MAIN_USER_ACCOUNT
            AccountTypeDto.PAYMENT_SYSTEM -> PAYMENT_SYSTEM
            AccountTypeDto.TRANSFER_COMMISSION -> TRANSFER_COMMISSION
        }
    }
}