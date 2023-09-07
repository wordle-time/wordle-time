package com.wordletime.config

data class ServerConfig(val host: String, val port: Int)
data class Config(val server: ServerConfig)
