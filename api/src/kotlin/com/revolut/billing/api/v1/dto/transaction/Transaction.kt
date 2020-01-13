package com.revolut.billing.api.v1.dto.transaction

import com.revolut.billing.api.v1.dto.accounts.AccountId
import java.math.BigDecimal
import java.util.UUID

class Transaction(
    val id: Long = 0,
    val operationId: UUID,
    val operationType: OperationType,
    val accountIdFrom: AccountId,
    val accountIdTo: AccountId,
    val amount: BigDecimal
)