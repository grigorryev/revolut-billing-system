package com.revolut.billing.api.v1.dto.transfer

import com.revolut.billing.api.v1.dto.accounts.AccountType
import java.math.BigDecimal
import java.util.UUID

class TransferRequest(
    val operationId: UUID,
    val subjectIdFrom: String,
    val accountTypeFrom: AccountType,
    val subjectIdTo: String,
    val accountTypeTo: AccountType,
    val currency: String,
    val amount: BigDecimal
)