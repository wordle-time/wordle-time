package com.wordletime.config

data class ServerConfig(val host: String, val port: Int)
data class WordProviderConfig(val staticWord: String)
data class Config(val server: ServerConfig, val wordProviderConfig: WordProviderConfig)
