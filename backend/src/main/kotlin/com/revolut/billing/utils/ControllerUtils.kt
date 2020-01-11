package com.revolut.billing.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import spark.Request
import spark.Response

val apiDtoMapper = jacksonObjectMapper()

inline fun <reified ReqT, ResT> controllerMethod(crossinline controllerMethod: (ReqT) -> ResT): (Request, Response) -> String {
    return { req: Request, res: Response ->
        val deserializedRequest = apiDtoMapper.readValue<ReqT>(req.body())
        val response = controllerMethod(deserializedRequest)
        apiDtoMapper.writeValueAsString(response)
    }
}