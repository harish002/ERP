package com.example.erp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform