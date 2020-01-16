package com.revolut.billing.api.v1.dto.transaction

import com.revolut.billing.api.v1.dto.accounts.AccountId
import java.math.BigDecimal
import java.util.UUID

/**
 * Transaction represents single transfer of money between two accounts.
 * Each operation creates one or more transactions.
 */
class Transaction(
    /**
     * Unique id of transaction.
     */
    val id: Long = 0,

    /**
     * Id of operation that creates this transaction.
     */
    val operationId: UUID,

    /**
     * Type of operation that creates this transaction.
     */
    val operationType: OperationType,

    /**
     * Id of account to transfer money from.
     */
    val accountIdFrom: AccountId,

    /**
     * Id of account to transfer money to.
     */
    val accountIdTo: AccountId,

    /**
     * Amount of money to transfer.
     */
    val amount: BigDecimal
)