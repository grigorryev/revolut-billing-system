package com.revolut.billing.exception

class ConstraintViolationException(val field: String, val reason: String) : RuntimeException()