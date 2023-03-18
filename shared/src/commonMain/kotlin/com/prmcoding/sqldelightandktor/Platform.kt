package com.prmcoding.sqldelightandktor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform