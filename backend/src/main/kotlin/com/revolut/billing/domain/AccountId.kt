package com.revolut.billing.domain

import com.revolut.billing.api.v1.dto.accounts.AccountId as AccountIdDto

data class AccountId(
    val type: AccountType,
    val subjectId: String,
    val currency: String
) : Comparable<AccountId> {

    override fun compareTo(other: AccountId): Int {
        return this.toString().compareTo(other.toString())
    }

    fun toDto() = AccountIdDto(
        subjectId = this.subjectId,
        type = this.type.toDto(),
        currency = this.currency
    )
}