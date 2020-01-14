package com.revolut.billing.utils

import feign.FeignException
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.with
import java.math.BigDecimal

fun httpStatus(status: Int) = { t: Throwable ->
    val feignException = t as FeignException
    feignException.status() shouldEqual status
}

fun shouldThrowBadRequest(action: () -> Any?) {
    action shouldThrow FeignException::class with httpStatus(400)
}

fun shouldThrowNotFound(action: () -> Any?) {
    action shouldThrow FeignException::class with httpStatus(404)
}

infix fun BigDecimal.shouldBeEquivalent(other: Long) {
    this.stripTrailingZeros() shouldEqual BigDecimal.valueOf(other).stripTrailingZeros()
}