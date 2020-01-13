package com.revolut.billing.exception

import java.lang.RuntimeException

class ConstraintViolationException(val field: String, val reason: String) : RuntimeException()