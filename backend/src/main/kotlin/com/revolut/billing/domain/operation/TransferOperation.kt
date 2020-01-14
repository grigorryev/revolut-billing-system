package com.revolut.billing.domain.operation

import com.revolut.billing.api.v1.dto.transfer.TransferRequest
import com.revolut.billing.domain.AccountId
import com.revolut.billing.domain.AccountType
import java.math.BigDecimal
import java.util.UUID

class TransferOperation(
    val operationId: UUID,
    val accountFrom: AccountId,
    val accountTo: AccountId,
    val currency: String,
    val amount: BigDecimal
) {
    companion object {
        fun fromDto(req: TransferRequest) = TransferOperation(
            operationId = req.operationId,
            accountFrom = AccountId(AccountType.fromDto(req.accountTypeFrom), req.subjectIdFrom, req.currency),
            accountTo = AccountId(AccountType.fromDto(req.accountTypeTo), req.subjectIdTo, req.currency),
            currency = req.currency,
            amount = req.amount
        )
    }
}