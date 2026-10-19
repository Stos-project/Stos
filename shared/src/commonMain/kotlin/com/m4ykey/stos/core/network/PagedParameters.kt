package com.m4ykey.stos.core.network

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter

fun HttpRequestBuilder.pagedParameters(
    page : Int,
    pageSize : Int,
    filter : String,
    order : String,
    sort : String
) {
    parameter("page", page)
    parameter("pagesize", pageSize)
    parameter("filter", filter)
    parameter("order", order)
    parameter("sort", sort.lowercase())
}