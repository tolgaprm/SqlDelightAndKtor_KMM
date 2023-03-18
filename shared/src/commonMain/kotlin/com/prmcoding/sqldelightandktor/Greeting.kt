package com.prmcoding.sqldelightandktor

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello this is my first project, ${platform.name}!"
    }
}