package com.revolut.billing.api.v1.dto.transfer

import java.math.BigDecimal
import java.util.UUID

class DepositRequest(
    /**
     * Unique id of operation.
     * Should be used to ensure idempotency. In case of retrying, client should
     * use the same operationId for each attempt to create this operation.
     */
    val operationId: UUID,

    /**
     * Id of the user receiving money.
     */
    val userId: String,

    /**
     * If of the payment system money come from
     */
    val paymentSystemId: String,

    /**
     * Amount of money to be deposited.
     */
    val amount: BigDecimal,

    /**
     * Standard 3-letter currency name.
     */
    val currency: String
)