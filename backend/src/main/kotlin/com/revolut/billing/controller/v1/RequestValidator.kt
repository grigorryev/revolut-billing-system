package com.revolut.billing.controller.v1

import com.revolut.billing.api.v1.dto.accounts.CreateAccountRequest
import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.api.v1.dto.transfer.TransferRequest
import com.revolut.billing.exception.ConstraintViolationException
import java.math.BigDecimal

/**
 * Throws ConstraintViolationException if incoming data fails validation.
 */
object RequestValidator {
    fun validate(request: DepositRequest) {
        if (request.userId.isEmpty()) throw ConstraintViolationException("userId", "should not be empty")
        if (request.paymentSystemId.isEmpty()) throw ConstraintViolationException("paymentSystemId", "should not be empty")
        if (request.amount <= BigDecimal.ZERO) throw ConstraintViolationException("amount", "should be positive")
        validateCurrency(request.currency)
    }

    fun validate(request: CreateAccountRequest) {
        if (request.subjectId.isEmpty()) throw ConstraintViolationException("subjectId", "should not be empty")
        validateCurrency(request.currency)
    }

    fun validateGetAccount(subjectId: String, currency: String) {
        if (subjectId.isEmpty()) throw ConstraintViolationException("subjectId", "should not be empty")
        validateCurrency(currency)
    }

    fun validateCurrency(currency: String) {
        if (currency.isEmpty()) throw ConstraintViolationException("currency", "should not be empty")
        if (currency.length > 3) throw ConstraintViolationException("currency", "should not be shorter than 3 chars")
    }

    fun validate(request: TransferRequest) {
        if (request.subjectIdFrom.isEmpty()) throw ConstraintViolationException("subjectIdFrom", "should not be empty")
        if (request.subjectIdTo.isEmpty()) throw ConstraintViolationException("subjectIdTo", "should not be empty")
        validateCurrency(request.currency)
        if (request.amount <= BigDecimal.ZERO) throw ConstraintViolationException("amount", "should be positive")
    }
}