package com.revolut.billing.controller.v1

import com.revolut.billing.api.v1.dto.transfer.DepositRequest
import com.revolut.billing.exception.ConstraintViolationException
import java.math.BigDecimal

object RequestValidator {
    fun validate(request: DepositRequest) {
        if (request.userId.isEmpty()) throw ConstraintViolationException("userId", "should not be empty")
        if (request.paymentSystemId.isEmpty()) throw ConstraintViolationException("paymentSystemId", "should not be empty")
        if (request.currency.isEmpty()) throw ConstraintViolationException("currency", "should not be empty")
        if (request.currency.length > 3) throw ConstraintViolationException("currency", "should not be shorter than 3 chars")
        if (request.amount <= BigDecimal.ZERO) throw ConstraintViolationException("amount", "should be positive")
    }
}