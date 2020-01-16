package com.revolut.billing.api.v1.dto.transfer

import com.revolut.billing.api.v1.dto.accounts.AccountType
import java.math.BigDecimal
import java.util.UUID

class TransferRequest(
    /**
     * Unique id of operation.
     * Should be used to ensure idempotency. In case of retrying, client should
     * use the same operationId for each attempt to create this operation.
     */
    val operationId: UUID,

    /**
     * `subjectId` of account to transfer money from.
     */
    val subjectIdFrom: String,

    /**
     * `accountType` of account to transfer money to.
     */
    val accountTypeFrom: AccountType,

    /**
     * `subjectId` of account to transfer money from.
     */
    val subjectIdTo: String,

    /**
     * `accountType` of account to transfer money to.
     */
    val accountTypeTo: AccountType,

    /**
     * Standard 3-letter currency name.
     */
    val currency: String,

    /**
     * Amount of money to be transferred.
     */
    val amount: BigDecimal
)