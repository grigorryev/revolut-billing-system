package com.revolut.billing.service

import com.google.common.util.concurrent.Striped
import com.google.inject.Inject
import com.google.inject.Singleton
import com.revolut.billing.DbClient
import com.revolut.billing.converter.DepositOperationConverter
import com.revolut.billing.converter.TransferOperationConverter
import com.revolut.billing.domain.Transaction
import com.revolut.billing.domain.operation.DepositOperation
import com.revolut.billing.domain.operation.TransferOperation
import com.revolut.billing.exception.InsufficientFundsException
import com.revolut.billing.exception.OperationAlreadyProcessedException
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

    fun deposit(operation: DepositOperation) {
        val transactions = DepositOperationConverter.convert(operation)
        process(operation.operationId, transactions)
    }

    fun transfer(operation: TransferOperation) {
        val transactions = TransferOperationConverter.convert(operation)
        process(operation.operationId, transactions)
    }

    private fun process(operationId: UUID, transactions: List<Transaction>) {
        // check if operation has been already processed
        throwIfAlreadyProcessed(operationId)

        // get involved accounts in sorted order
        val accountsToLock = transactions.flatMap { listOf(it.accountIdFrom, it.accountIdTo) }
            .sorted()

        try {
            // acquire the lock for each account
            accountsToLock.forEach { account ->
                accountLocks.get(account).lock()
            }

            // double check that operation hasn't been processed yet
            throwIfAlreadyProcessed(operationId)

            // do the actual transfer
            performTransactions(transactions)
        } finally {
            // release locks in reversed order to prevent deadlocks
            accountsToLock.reversed().forEach { account ->
                accountLocks.get(account).unlock()
            }
        }
    }

    private fun performTransactions(transactions: List<Transaction>) {
        // process all transactions within a single database transaction
        db.ctx().transactionResult { conf ->
            val dbTransactionContext = DSL.using(conf)
            transactions.forEach { tx -> performTransaction(tx, dbTransactionContext) }
        }
    }

    private fun performTransaction(tx: Transaction, dbTransactionContext: DSLContext) {
        val from = accountService.getOrCreateAccount(tx.accountIdFrom, dbTransactionContext)
        val to = accountService.getOrCreateAccount(tx.accountIdTo, dbTransactionContext)

        // throw if account doesn't have enough money
        if (!from.canBeNegative() && from.amount < tx.amount) {
            throw InsufficientFundsException(tx.operationId)
        }

        transactionRepository.save(tx, dbTransactionContext)

        accountService.updateAccount(from.decreasedBy(tx.amount), dbTransactionContext)
        accountService.updateAccount(to.increasedBy(tx.amount), dbTransactionContext)
    }

    private fun throwIfAlreadyProcessed(operationId: UUID) {
        val transactions = transactionRepository.findByOperationId(operationId, db.ctx())
        if (transactions.isNotEmpty()) throw OperationAlreadyProcessedException(operationId)
    }
}