package com.lbm.lbmapp.utils

expect class Platform {
    companion object {
        fun isDesktop(): Boolean
    }
}