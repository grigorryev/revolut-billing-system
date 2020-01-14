package com.revolut.billing.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import spark.Request
import spark.Response

object ApiObjectMapper {
    private val mapper = jacksonObjectMapper()
        .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
        .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)

    fun get() = mapper
}

inline fun <reified ReqT, ResT> postMethod(crossinline method: (ReqT) -> ResT): (Request, Response) -> String {
    return { req: Request, res: Response ->
        val deserializedRequest = ApiObjectMapper.get().readValue<ReqT>(req.body())
        val response = method(deserializedRequest)
        ApiObjectMapper.get().writeValueAsString(response)
    }
}