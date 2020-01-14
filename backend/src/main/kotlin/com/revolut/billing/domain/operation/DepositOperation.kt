package com.revolut.billing.domain.operation

import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.domain.AccountId
import com.revolut.billing.domain.AccountType
import java.math.BigDecimal
import java.util.UUID

class DepositOperation(
    val operationId: UUID,
    val userAccountId: AccountId,
    val paymentSystemAccountId: AccountId,
    val amount: BigDecimal,
    val currency: String
) {
    companion object {
        fun fromDto(req: DepositRequest) = DepositOperation(
            operationId = req.operationId,
            userAccountId = AccountId(AccountType.MAIN_USER_ACCOUNT, req.userId, req.currency),
            paymentSystemAccountId = AccountId(AccountType.PAYMENT_SYSTEM, req.paymentSystemId, req.currency),
            amount = req.amount,
            currency = req.currency
        )
    }
}