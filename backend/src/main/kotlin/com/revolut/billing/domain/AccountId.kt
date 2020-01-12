package com.revolut.billing.domain

data class AccountId(
    val type: AccountType,
    val subjectId: String,
    val currency: String
) : Comparable<AccountId> {

    override fun compareTo(other: AccountId): Int {
        return this.toString().compareTo(other.toString())
    }
}