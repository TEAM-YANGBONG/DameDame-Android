package com.yangbong.core_data.exception

class RetrofitFailureStateException(error: String?, val code: Int) : Exception(error)
