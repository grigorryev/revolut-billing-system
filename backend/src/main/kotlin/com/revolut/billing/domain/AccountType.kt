package com.revolut.billing.domain

import com.revolut.billing.api.v1.dto.accounts.AccountType as AccountTypeDto

enum class AccountType {
    MAIN_USER_ACCOUNT;

    fun toDto(): AccountTypeDto = when (this) {
        MAIN_USER_ACCOUNT -> AccountTypeDto.MAIN_USER_ACCOUNT
    }

    companion object {
        fun fromDto(dto: AccountTypeDto) = when (dto) {
            AccountTypeDto.MAIN_USER_ACCOUNT -> MAIN_USER_ACCOUNT
        }
    }
}