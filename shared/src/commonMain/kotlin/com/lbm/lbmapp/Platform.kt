package com.lbm.lbmapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform