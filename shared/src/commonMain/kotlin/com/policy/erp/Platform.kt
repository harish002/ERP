package com.policy.erp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform