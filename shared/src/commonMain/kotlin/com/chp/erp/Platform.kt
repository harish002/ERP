package com.chp.erp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform