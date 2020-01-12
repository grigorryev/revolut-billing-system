package com.revolut.billing.api.v1.dto.transfer

import java.math.BigDecimal
import java.util.UUID

class WithdrawRequest(
    val operationId: UUID,
    val userId: String,
    val amount: BigDecimal,
    val currency: String
)