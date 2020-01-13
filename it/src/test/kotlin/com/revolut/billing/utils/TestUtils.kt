package com.revolut.billing.utils

import feign.FeignException
import org.amshove.kluent.shouldEqual

fun httpStatus(status: Int) = { t: Throwable ->
    val feignException = t as FeignException
    feignException.status() shouldEqual status
}