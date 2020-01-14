package com.revolut.billing.exception

import spark.Spark.exception

fun registerExceptionHandlers() {
    exception(OperationAlreadyProcessedException::class.java) { ex, req, res ->
        res.status(409) // conflict
        res.body("Operation ${ex.operationId} already processed")
    }

    exception(ConstraintViolationException::class.java) { ex, req, res ->
        res.status(400) // bad request
        res.body("Constraint violation. ${ex.field}: ${ex.reason}")
    }

    exception(NoAccountFoundException::class.java) { ex, req, res ->
        res.status(404) // not found
        res.body("No account found")
    }

    exception(InsufficientFundsException::class.java) { ex, req, res ->
        res.status(403) // forbidden
        res.body("Insufficient funds")
    }
}