package com.revolut.billing.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.revolut.billing.controller.v1.RequestValidator
import spark.Request
import spark.Response

val apiDtoMapper = jacksonObjectMapper()
    .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
    .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)

inline fun <reified ReqT, ResT> postMethod(crossinline method: (ReqT) -> ResT): (Request, Response) -> String {
    return { req: Request, res: Response ->
        val deserializedRequest = apiDtoMapper.readValue<ReqT>(req.body())
        val response = method(deserializedRequest)
        apiDtoMapper.writeValueAsString(response)
    }
}