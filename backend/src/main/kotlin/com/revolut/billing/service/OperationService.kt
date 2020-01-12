package com.revolut.billing.service

import com.google.common.util.concurrent.Striped
import com.google.inject.Inject
import com.google.inject.Singleton
import com.revolut.billing.DbClient
import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.api.v1.dto.transfer.OperationResponse
import com.revolut.billing.domain.Transaction
import com.revolut.billing.repository.TransactionRepository
import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.util.UUID

@Singleton
class OperationService @Inject constructor(
    private val accountService: AccountService,
    private val transactionRepository: TransactionRepository,
    private val db: DbClient
) {

    private val accountLocks = Striped.lock(64)

    fun deposit(depositRequest: DepositRequest): OperationResponse {
        val transactions = DepositProcessor.process(depositRequest)
        process(depositRequest.operationId, transactions)

        return OperationResponse(depositRequest.operationId)
    }

//    fun withdraw(depositRequest: DepositRequest): OperationResponse {
//
//    }
//
//    fun transfer(depositRequest: DepositRequest): OperationResponse {
//
//    }

    private fun process(operationId: UUID, transactions: List<Transaction>) {
        // checkIfAlreadyProcessed(operationId)

        val accountsToLock = transactions.flatMap { listOf(it.accountIdFrom, it.accountIdTo) }
            .sorted()

        try {
            accountsToLock.forEach { account ->
                accountLocks.get(account).lock()
            }

            performTransactions(transactions)
            // checkIfAlreadyProcessed(operationId)


        } finally {
            accountsToLock.reversed().forEach { account ->
                accountLocks.get(account).unlock()
            }
        }

    }

    private fun performTransactions(transactions: List<Transaction>) {
        db.ctx().transactionResult { conf ->
            val dbTransactionContext = DSL.using(conf)
            transactions.forEach { tx -> performTransaction(tx, dbTransactionContext) }
        }
    }

    private fun performTransaction(tx: Transaction, dbTransactionContext: DSLContext) {
        val from = accountService.getOrCreateAccount(tx.accountIdFrom, dbTransactionContext)
        val to = accountService.getOrCreateAccount(tx.accountIdTo, dbTransactionContext)

        if (!from.canBeNegative() && from.amount < tx.amount) {
            throw IllegalStateException("not enough money")
        }

        transactionRepository.save(tx, dbTransactionContext)

        accountService.updateAccount(from.decreasedBy(tx.amount), dbTransactionContext)
        accountService.updateAccount(to.increasedBy(tx.amount), dbTransactionContext)
    }
}