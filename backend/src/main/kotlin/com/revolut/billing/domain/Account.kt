package com.revolut.billing.domain

import db.tables.Account.ACCOUNT
import org.jooq.Record
import java.math.BigDecimal
import com.revolut.billing.api.v1.dto.accounts.Account as AccountDto

class Account(
    val id: Long,
    val accountId: AccountId,
    val amount: BigDecimal
) {
    fun toDto() = AccountDto(
        type = this.accountId.type.toDto(),
        subjectId = this.accountId.subjectId,
        currency = this.accountId.currency,
        amount = this.amount
    )

    fun canBeNegative(): Boolean = this.accountId.type.canBeNegative()

    fun decreasedBy(delta: BigDecimal) = Account(
        id = this.id,
        accountId = this.accountId,
        amount = this.amount.minus(delta)
    )

    fun increasedBy(delta: BigDecimal) = Account(
        id = this.id,
        accountId = this.accountId,
        amount = this.amount.minus(delta)
    )

    companion object {
        fun fromDbRecord(record: Record) = Account(
            id = record.getValue(ACCOUNT.ID),
            accountId = AccountId(
                type = AccountType.valueOf(record.getValue(ACCOUNT.ACCOUNT_TYPE)),
                subjectId = record.getValue(ACCOUNT.SUBJECT_ID),
                currency = record.getValue(ACCOUNT.CURRENCY)
            ),
            amount = record.getValue(ACCOUNT.AMOUNT)
        )
    }
}