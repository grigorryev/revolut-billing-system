package com.revolut.billing.service

import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.domain.AccountId
import com.revolut.billing.domain.AccountType
import com.revolut.billing.domain.OperationType
import com.revolut.billing.domain.Transaction

object DepositProcessor {
    fun process(request: DepositRequest): List<Transaction> {
        val accountIdFrom = AccountId(
            type = AccountType.PAYMENT_SYSTEM,
            subjectId = request.paymentSystemId,
            currency = request.currency
        )

        val accountIdTo = AccountId(
            type = AccountType.MAIN_USER_ACCOUNT,
            subjectId = request.userId,
            currency = request.currency
        )

        val depositTransaction = Transaction(
            operationId = request.operationId,
            operationType = OperationType.DEPOSIT,
            accountIdFrom = accountIdFrom,
            accountIdTo = accountIdTo,
            amount = request.amount
        )

        return listOf(depositTransaction)
    }
}