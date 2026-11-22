package com.m4ykey.stos.core.network

class BackoffException(val retryAfterSeconds : Long) : Exception("Too many requests. Try again in $retryAfterSeconds s.")